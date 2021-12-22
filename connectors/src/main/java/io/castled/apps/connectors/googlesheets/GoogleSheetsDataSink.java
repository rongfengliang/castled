package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import io.castled.ObjectRegistry;
import io.castled.apps.DataSink;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.schema.models.Field;
import io.castled.schema.models.Message;
import io.castled.warehouses.connectors.bigquery.daos.ServiceAccountDetailsDAO;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GoogleSheetsDataSink implements DataSink {

    private GoogleSheetsObjectSink googleSheetsObjectSink;

    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {
        GoogleSheetsAppConfig googleSheetsAppConfig = (GoogleSheetsAppConfig) dataSinkRequest.getExternalApp().getConfig();
        GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig = (GoogleSheetsAppSyncConfig) dataSinkRequest.getAppSyncConfig();
        ServiceAccountDetails serviceAccountDetails =
                ObjectRegistry.getInstance(Jdbi.class).onDemand(ServiceAccountDetailsDAO.class)
                        .getServiceAccount(googleSheetsAppConfig.getServiceAccount()).getServiceAccountDetails();

        Sheets sheetsService = GoogleSheetUtils.getSheets(serviceAccountDetails);
        List<SheetRow> sheetRows = GoogleSheetUtils.getRows(sheetsService, googleSheetsAppConfig.getSpreadSheetId(),
                googleSheetsAppSyncConfig.getObject().getObjectName());

        sheetsService.spreadsheets().values().clear(googleSheetsAppConfig.getSpreadSheetId(), googleSheetsAppSyncConfig.getObject().getObjectName(),
                new ClearValuesRequest()).execute();

        this.googleSheetsObjectSink = new GoogleSheetsObjectSink(googleSheetsAppConfig, googleSheetsAppSyncConfig, sheetsService,
                sheetRows, dataSinkRequest.getPrimaryKeys(), dataSinkRequest.getMappedFields());

        ;

        Message message;
        int recordsCount = 0;
        while ((message = dataSinkRequest.getMessageInputStream().readMessage()) != null) {
            if (recordsCount == 0) {
                sheetsService.spreadsheets().values().append(googleSheetsAppConfig.getSpreadSheetId(), googleSheetsAppSyncConfig.getObject().getObjectName(),
                                new ValueRange().setValues(Collections.singletonList(new ArrayList<>(dataSinkRequest.getMappedFields()))))
                        .setValueInputOption("USER_ENTERED").execute();
            }
            this.googleSheetsObjectSink.writeRecord(message);
            recordsCount++;
        }
        this.googleSheetsObjectSink.flushRecords();
    }

    @Override
    public AppSyncStats getSyncStats() {
        return Optional.ofNullable(this.googleSheetsObjectSink)
                .map(audienceSinkRef -> this.googleSheetsObjectSink.getSyncStats())
                .map(statsRef -> new AppSyncStats(statsRef.getRecordsProcessed(), statsRef.getOffset(), 0))
                .orElse(new AppSyncStats(0, 0, 0));
    }
}
