package io.castled.apps.connectors.restapi;


import com.google.inject.Singleton;
import io.castled.commons.errors.CastledError;
import io.castled.commons.errors.errorclassifications.UnclassifiedError;

@Singleton
public class RestApiErrorParser {

    public CastledError getPipelineError(String failureReason) {
        return new UnclassifiedError(failureReason);
    }

}
