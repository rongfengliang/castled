package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import io.castled.apps.DataSink;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;
import io.castled.schema.models.Message;

import java.util.Optional;

public class GoogleSheetsDataSink implements DataSink {

    private GoogleSheetsObjectSink googleSheetsObjectSink;

    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {
        GoogleSheetsAppConfig googleSheetsAppConfig = (GoogleSheetsAppConfig) dataSinkRequest.getExternalApp().getConfig();
        GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig = (GoogleSheetsAppSyncConfig) dataSinkRequest.getAppSyncConfig();
        Sheets sheetsService = GoogleSheetUtils.getSheets(googleSheetsAppConfig.getServiceAccountDetails());

        sheetsService.spreadsheets().values().clear(googleSheetsAppConfig.getSpreadSheetId(), googleSheetsAppSyncConfig.getObject().getObjectName(),
                new ClearValuesRequest()).execute();
        this.googleSheetsObjectSink = new GoogleSheetsObjectSink(googleSheetsAppConfig, googleSheetsAppSyncConfig, sheetsService);

        Message message;
        while ((message = dataSinkRequest.getMessageInputStream().readMessage()) != null) {
            this.googleSheetsObjectSink.writeRecord(message);
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
