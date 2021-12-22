package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.castled.apps.BufferedObjectSink;
import io.castled.commons.models.MessageSyncStats;
import io.castled.exceptions.CastledRuntimeException;
import io.castled.schema.models.Field;
import io.castled.schema.models.Message;
import io.castled.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.internal.apache.arrow.flatbuf.Int;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GoogleSheetsObjectSink extends BufferedObjectSink<Message> {

    private final GoogleSheetsAppConfig googleSheetsAppConfig;
    private final GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig;
    private final List<String> primaryKeys;
    private final AtomicLong processedRecords = new AtomicLong(0);
    private long lastProcessedOffset = 0;
    private final Sheets sheetsService;
    private final Map<Integer, SheetRow> hashedRows;
    private final List<String> headers;

    public GoogleSheetsObjectSink(GoogleSheetsAppConfig googleSheetsAppConfig, GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig,
                                  Sheets sheetsService, List<SheetRow> sheetRows, List<String> primaryKeys, List<String> headers) {
        this.googleSheetsAppConfig = googleSheetsAppConfig;
        this.googleSheetsAppSyncConfig = googleSheetsAppSyncConfig;
        this.sheetsService = sheetsService;
        this.hashedRows = Optional.ofNullable(sheetRows)
                .map(row -> row.stream().collect(Collectors.toMap(rowRef -> GoogleSheetUtils.getPrimaryKeysHash(rowRef, primaryKeys), Function.identity())))
                .orElse(Maps.newHashMap());
        this.primaryKeys = primaryKeys;
        this.headers = headers;
    }

    @Override
    protected void writeRecords(List<Message> messages) {
        try {
            List<List<Object>> values = Lists.newArrayList();
            for (Message message : messages) {
                SheetRow sheetRow = new SheetRow(headers.stream()
                        .collect(Collectors.toMap(Function.identity(), header -> GoogleSheetUtils.getSheetsValue(message.getRecord().getField(header)), (v1, v2) -> v1, LinkedHashMap::new)));
                values.add(new ArrayList<>(sheetRow.getValues().values()));
                hashedRows.remove(GoogleSheetUtils.getPrimaryKeysHash(sheetRow, primaryKeys));
            }
            ValueRange valueRange = new ValueRange().setValues(values);
            sheetsService.spreadsheets()
                    .values().append(googleSheetsAppConfig.getSpreadSheetId(),
                            googleSheetsAppSyncConfig.getObject().getObjectName(), valueRange).setValueInputOption("USER_ENTERED").execute();
        } catch (Exception e) {
            log.error("Google Sheets append records failed", e);
            throw new CastledRuntimeException(e);
        }
        this.processedRecords.addAndGet(messages.size());
        this.lastProcessedOffset = Math.max(lastProcessedOffset, messages.get(messages.size() - 1).getOffset());

    }

    public void afterRecordsFlush() {
        try {
            if (!MapUtils.isEmpty(hashedRows)) {
                List<List<Object>> rowValues = Lists.newArrayList();
                if (processedRecords.get() == 0) {
                    rowValues.add(new ArrayList<>(Lists.newArrayList(hashedRows.values()).get(0).getValues().keySet()));
                }
                rowValues.addAll(hashedRows.values().stream()
                        .map(valueRef -> new ArrayList<>(valueRef.getValues().values())).collect(Collectors.toList()));
                ValueRange valueRange = new ValueRange().setValues(rowValues);
                sheetsService.spreadsheets()
                        .values().append(googleSheetsAppConfig.getSpreadSheetId(),
                                googleSheetsAppSyncConfig.getObject().getObjectName(), valueRange).setValueInputOption("USER_ENTERED").execute();
            }
        } catch (Exception e) {
            log.error("Google Sheets append records failed", e);
            throw new CastledRuntimeException(e);
        }
    }

    public MessageSyncStats getSyncStats() {
        return new MessageSyncStats(processedRecords.get(), lastProcessedOffset);
    }

    @Override
    public long getMaxBufferedObjects() {
        return 10000;
    }
}
