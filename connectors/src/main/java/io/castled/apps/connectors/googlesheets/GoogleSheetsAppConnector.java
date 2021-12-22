package io.castled.apps.connectors.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppConnector;
import io.castled.apps.models.ExternalAppSchema;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.exceptions.CastledRuntimeException;
import io.castled.forms.dtos.FormFieldOption;
import io.castled.warehouses.connectors.bigquery.daos.ServiceAccountDetailsDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class GoogleSheetsAppConnector implements ExternalAppConnector<GoogleSheetsAppConfig,
        GoogleSheetsDataSink, GoogleSheetsAppSyncConfig> {

    @Override
    public List<FormFieldOption> getAllObjects(GoogleSheetsAppConfig config, GoogleSheetsAppSyncConfig mappingConfig) {
        try {
            ServiceAccountDetails serviceAccountDetails =
                    ObjectRegistry.getInstance(Jdbi.class).onDemand(ServiceAccountDetailsDAO.class)
                            .getServiceAccount(config.getServiceAccount()).getServiceAccountDetails();
            Sheets sheetsService = GoogleSheetUtils.getSheets(serviceAccountDetails);
            Spreadsheet spreadsheet = sheetsService.spreadsheets().get(config.getSpreadSheetId()).execute();
            return spreadsheet.getSheets().stream().map(Sheet::getProperties)
                    .map(sheetProperties -> new FormFieldOption(new GoogleSheetsSyncObject(sheetProperties.getSheetId(), sheetProperties.getTitle()),
                            sheetProperties.getTitle()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Gsheets get objects failed for {}", config.getServiceAccount(), e);
            throw new CastledRuntimeException(e);
        }
    }

    @Override
    public GoogleSheetsDataSink getDataSink() {
        return ObjectRegistry.getInstance(GoogleSheetsDataSink.class);
    }

    @Override
    public ExternalAppSchema getSchema(GoogleSheetsAppConfig config, GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig) {
        return new ExternalAppSchema(null, Lists.newArrayList());
    }

    @Override
    public Class<GoogleSheetsAppSyncConfig> getMappingConfigType() {
        return GoogleSheetsAppSyncConfig.class;
    }

    @Override
    public Class<GoogleSheetsAppConfig> getAppConfigType() {
        return GoogleSheetsAppConfig.class;
    }
}
