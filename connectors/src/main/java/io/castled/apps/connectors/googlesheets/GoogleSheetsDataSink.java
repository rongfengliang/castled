package io.castled.apps.connectors.googlesheets;

import io.castled.apps.DataSink;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;

public class GoogleSheetsDataSink implements DataSink {
    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {

    }

    @Override
    public AppSyncStats getSyncStats() {
        return null;
    }
}
