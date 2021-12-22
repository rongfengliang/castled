package io.castled.apps.connectors.googlesheets;

import com.google.inject.Inject;
import io.castled.apps.AppConfig;
import io.castled.apps.optionfetchers.AppOptionsFetcher;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.forms.dtos.FormFieldOption;
import io.castled.utils.JsonUtils;
import io.castled.warehouses.connectors.bigquery.GcpServiceAccount;
import io.castled.warehouses.connectors.bigquery.daos.ServiceAccountDetailsDAO;
import org.jdbi.v3.core.Jdbi;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class GSheetsServiceAccountFetcher implements AppOptionsFetcher {

    private final ServiceAccountDetailsDAO serviceAccountDetailsDAO;

    @Inject
    public GSheetsServiceAccountFetcher(Jdbi jdbi) {
        this.serviceAccountDetailsDAO = jdbi.onDemand(ServiceAccountDetailsDAO.class);
    }

    @Override
    public List<FormFieldOption> getFieldOptions(AppConfig appConfig) {
        GoogleSheetsAppConfig googleSheetsAppConfig = (GoogleSheetsAppConfig) appConfig;
        ServiceAccountDetails serviceAccountDetails = googleSheetsAppConfig.getServiceAccountDetails();
        GcpServiceAccount gcpServiceAccount = serviceAccountDetailsDAO.getServiceAccount(serviceAccountDetails.getClientEmail());
        if (gcpServiceAccount == null) {
            serviceAccountDetailsDAO.createServiceAccountDetails(serviceAccountDetails.getClientEmail(),
                    Base64.getEncoder().encodeToString(JsonUtils.objectToByteArray(serviceAccountDetails)));
        }
        return Collections.singletonList(new FormFieldOption(googleSheetsAppConfig.getServiceAccountDetails().getClientEmail(),
                googleSheetsAppConfig.getServiceAccountDetails().getClientEmail()));

    }
}
