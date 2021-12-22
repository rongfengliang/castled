package io.castled.apps.connectors.googlesheets;

import io.castled.apps.AppConfig;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleSheetsAppConfig extends AppConfig {

    @FormField(type = FormFieldType.TEXT_BOX, title = "Spread Sheet Id", description = "Spread Sheet Id", schema = FormFieldSchema.STRING)
    private String spreadSheetId;

    @FormField(type = FormFieldType.JSON_FILE, description = "Service Account Json File", title = "Service Account Json File")
    private ServiceAccountDetails serviceAccountDetails;
}
