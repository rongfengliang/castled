package io.castled.apps.connectors.restapi;

import io.castled.exceptions.CastledRuntimeException;
import lombok.Getter;

import java.util.Arrays;

public enum RestApiObject {

    POST("POST");

    @Getter
    private final String name;

    RestApiObject(String name) {
        this.name = name;
    }

    public static RestApiObject getObjectByName(String name) {
        return Arrays.stream(RestApiObject.values()).filter(restApiObject -> restApiObject.getName().equals(name))
                .findFirst().orElseThrow(() -> new CastledRuntimeException(String.format("Invalid object name %s", name)));
    }

}
