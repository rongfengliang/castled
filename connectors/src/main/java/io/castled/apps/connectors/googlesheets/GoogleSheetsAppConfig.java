package io.castled.apps.connectors.googlesheets;

import io.castled.apps.AppConfig;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@HelpText(value = "Provide Editor access of your google sheets to email ${serviceAccount.client_email}", dependencies = {"spreadSheetId", "serviceAccount"})
@GroupActivator(dependencies = {"spreadSheetId"}, group = "service_account")
public class GoogleSheetsAppConfig extends AppConfig {

    @FormField(type = FormFieldType.TEXT_BOX, title = "Spread Sheet Url", placeholder = "https://docs.google.com/spreadsheets/d/{spreadsheetId}/edit#gid=0", description = "Spread Sheet Id", schema = FormFieldSchema.STRING)
    private String spreadSheetId;

    @FormField(type = FormFieldType.JSON_FILE, description = "Service Account Json File", title = "Service Account Json File", group = "service_account")
    private ServiceAccountDetails serviceAccount;
}
