package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
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
        Sheets sheetsService = GoogleSheetUtils.getSheets(googleSheetsAppConfig.getServiceAccountDetails());

        this.googleSheetsObjectSink = new GoogleSheetsObjectSink(
                googleSheetsAppConfig, (GoogleSheetsAppSyncConfig) dataSinkRequest.getAppSyncConfig(), sheetsService);

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
