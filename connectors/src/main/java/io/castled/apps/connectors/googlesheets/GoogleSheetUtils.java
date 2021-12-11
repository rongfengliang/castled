package io.castled.apps.connectors.googlesheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.utils.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class GoogleSheetUtils {

    public static Sheets getSheets(ServiceAccountDetails serviceAccountDetails) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials googleCredentials;
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
        try (InputStream serviceAccountStream = new ByteArrayInputStream(JsonUtils.objectToByteArray(serviceAccountDetails))) {
            googleCredentials = GoogleCredentials.fromStream(serviceAccountStream).createScoped(SCOPES);
        }
        return new Sheets.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName("Castled")
                .build();
    }
}
