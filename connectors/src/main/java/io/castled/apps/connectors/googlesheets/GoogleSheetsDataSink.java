package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import io.castled.apps.DataSink;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;

public class GoogleSheetsDataSink implements DataSink {
    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {
        GoogleSheetsAppConfig googleSheetsAppConfig = (GoogleSheetsAppConfig) dataSinkRequest.getExternalApp().getConfig();
        Sheets sheetsService = GoogleSheetUtils.getSheets(googleSheetsAppConfig.getServiceAccountDetails());
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(googleSheetsAppConfig.getSpreadSheetId()).execute();
    }

    @Override
    public AppSyncStats getSyncStats() {
        return null;
    }
}
