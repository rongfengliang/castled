package io.castled.utils;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.exceptions.CastledRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Slf4j
public class GcpUtils {

    public static Credentials getCredentials(ServiceAccountDetails serviceAccountDetails) {
        try {
            InputStream serviceAccountStream = new ByteArrayInputStream(JsonUtils.objectToByteArray(serviceAccountDetails));
            return GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            log.error(String.format("Service account credentials fetch failed for %s", serviceAccountDetails.getClientEmail()));
            throw new CastledRuntimeException(e);
        }
    }
}
