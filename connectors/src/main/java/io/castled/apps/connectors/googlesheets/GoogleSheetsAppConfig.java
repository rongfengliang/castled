package io.castled.apps.connectors.googlesheets;

import io.castled.OptionsReferences;
import io.castled.apps.AppConfig;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupActivator(dependencies = {"serviceAccountDetails"}, group = "serviceAccount")
public class GoogleSheetsAppConfig extends AppConfig {

    @FormField(type = FormFieldType.TEXT_BOX, title = "Spread Sheet Id", description = "Spread Sheet Id", schema = FormFieldSchema.STRING)
    private String spreadSheetId;

    @FormField(type = FormFieldType.JSON_FILE, description = "Service Account Json File", title = "Service Account Json File")
    private ServiceAccountDetails serviceAccountDetails;

    @FormField(type = FormFieldType.TEXT_BOX, description = "Service Account", title = "Provide Editor Access of your sheet to this email", group = "serviceAccount",
            optionsRef = @OptionsRef(value = OptionsReferences.GSHEETS_SERVICE_ACCOUNT, type = OptionsRefType.DYNAMIC))
    private String serviceAccount;

}
