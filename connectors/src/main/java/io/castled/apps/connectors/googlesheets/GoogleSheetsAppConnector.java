package io.castled.apps.connectors.googlesheets;

import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppConnector;
import io.castled.apps.models.ExternalAppSchema;
import io.castled.forms.dtos.FormFieldOption;

import java.util.List;

public class GoogleSheetsAppConnector implements ExternalAppConnector<GoogleSheetsAppConfig,
        GoogleSheetsDataSink, GoogleSheetsAppSyncConfig> {

    @Override
    public List<FormFieldOption> getAllObjects(GoogleSheetsAppConfig config, GoogleSheetsAppSyncConfig mappingConfig) {
        return null;
    }

    @Override
    public GoogleSheetsDataSink getDataSink() {
        return ObjectRegistry.getInstance(GoogleSheetsDataSink.class);
    }

    @Override
    public ExternalAppSchema getSchema(GoogleSheetsAppConfig config, GoogleSheetsAppSyncConfig googleSheetsAppSyncConfig) {
        return null;
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
