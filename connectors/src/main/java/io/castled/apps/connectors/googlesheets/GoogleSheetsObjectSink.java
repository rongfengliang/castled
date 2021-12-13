package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Lists;
import io.castled.apps.BufferedObjectSink;
import io.castled.commons.models.MessageSyncStats;
import io.castled.schema.models.Message;
import io.castled.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class GoogleSheetsObjectSink extends BufferedObjectSink<Message> {

    private final GoogleSheetsAppConfig googleSheetsAppConfig;
    private final GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig;
    private final AtomicLong processedRecords = new AtomicLong(0);
    private long lastProcessedOffset = 0;
    private final Sheets sheetsService;

    public GoogleSheetsObjectSink(GoogleSheetsAppConfig googleSheetsAppConfig, GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig,
                                  Sheets sheetsService) {
        this.googleSheetsAppConfig = googleSheetsAppConfig;
        this.googleSheetsAppSyncConfig = googleSheetsAppSyncConfig;
        this.sheetsService = sheetsService;
    }

    @Override
    protected void writeRecords(List<Message> messages) {
        try {
            ValueRange valueRange = new ValueRange();
            List<List<Object>> values = Lists.newArrayList();
            for (Message message : messages) {
                values.add(message.getRecord().getFields().stream().map(MessageUtils::getJsonValue).collect(Collectors.toList()));
            }
            valueRange.setValues(values);
            sheetsService.spreadsheets()
                    .values().append(googleSheetsAppConfig.getSpreadSheetId(), googleSheetsAppSyncConfig.getObject().getObjectName() + "!A1:B1",
                            valueRange).setValueInputOption("USER_ENTERED").execute();
        } catch (Exception e) {
            log.error("Write records failed", e);
        }
        this.processedRecords.addAndGet(messages.size());
        this.lastProcessedOffset = Math.max(lastProcessedOffset, messages.get(messages.size() - 1).getOffset());

    }

    public MessageSyncStats getSyncStats() {
        return new MessageSyncStats(processedRecords.get(), lastProcessedOffset);
    }

    @Override
    public long getMaxBufferedObjects() {
        return 10000;
    }
}
