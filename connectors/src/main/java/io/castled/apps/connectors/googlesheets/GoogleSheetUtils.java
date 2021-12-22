package io.castled.apps.connectors.googlesheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.exceptions.CastledRuntimeException;
import io.castled.schema.models.Field;
import io.castled.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GoogleSheetUtils {

    public static Sheets getSheets(ServiceAccountDetails serviceAccountDetails) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials googleCredentials;
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        try (InputStream serviceAccountStream = new ByteArrayInputStream(JsonUtils.objectToByteArray(serviceAccountDetails))) {
            googleCredentials = GoogleCredentials.fromStream(serviceAccountStream).createScoped(SCOPES);
        }
        return new Sheets.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName("Castled")
                .build();
    }

    public static List<SheetRow> getRows(Sheets sheetsService, String spreadSheetId, String sheetName) throws IOException {
        List<List<Object>> sheetEntries = sheetsService.spreadsheets().values()
                .get(spreadSheetId, sheetName).execute().getValues();
        if (CollectionUtils.isEmpty(sheetEntries) || sheetEntries.size() == 1) {
            return null;
        }
        List<String> headers = sheetEntries.get(0).stream().map(String::valueOf).collect(Collectors.toList());
        List<List<Object>> rowValues = sheetEntries.subList(1, sheetEntries.size());
        if (rowValues.stream().anyMatch(row -> row.size() != headers.size())) {
            throw new CastledRuntimeException("Row size does not match the header size");
        }
        List<SheetRow> sheetRows = Lists.newArrayList();
        for (List<Object> rowValue : rowValues) {
            sheetRows.add(new SheetRow(IntStream.range(0, headers.size()).boxed()
                    .collect(Collectors.toMap(headers::get, rowValue::get, (v1, v2) -> v1, LinkedHashMap::new))));
        }
        return sheetRows;
    }

    public static Integer getPrimaryKeysHash(SheetRow sheetRow, List<String> primaryKeys) {
        return primaryKeys.stream().map(primaryKey -> sheetRow.getValues().get(primaryKey)).collect(Collectors.toList()).hashCode();
    }

    public static Object getSheetsValue(Field field) {
        if (field == null) {
            return "";
        }
        switch (field.getSchema().getType()) {
            case DATE:
                LocalDate localDate = (LocalDate) field.getValue();
                return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            case TIMESTAMP:
                LocalDateTime localDateTime = (LocalDateTime) field.getValue();
                return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            case ZONED_TIMESTAMP:
                ZonedDateTime zonedDateTime = (ZonedDateTime) field.getValue();
                return zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
            case TIME:
                LocalTime localTime = (LocalTime) field.getValue();
                return localTime.format(DateTimeFormatter.ISO_TIME);
            case INT:
            case LONG:
                return String.valueOf(field.getValue());
            default:
                return field.getValue();
        }

    }
}