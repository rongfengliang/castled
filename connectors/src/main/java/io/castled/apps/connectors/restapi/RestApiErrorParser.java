package io.castled.apps.connectors.restapi;


import com.google.inject.Singleton;
import io.castled.commons.errors.CastledError;
import io.castled.commons.errors.errorclassifications.ExternallyCategorizedError;

@Singleton
public class RestApiErrorParser {

    public CastledError getPipelineError(String errorCode, String description) {
        return new ExternallyCategorizedError(errorCode, description);
    }
}
