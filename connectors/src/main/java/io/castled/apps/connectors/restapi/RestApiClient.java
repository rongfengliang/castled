package io.castled.apps.connectors.restapi;

import io.castled.ObjectRegistry;
import io.castled.utils.ResponseUtils;
import io.castled.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RestApiClient {

    public static final String BEARER_AUTHENTICATION = "Bearer ";
    private final Client client;
    private final String apiKey;
    private final String apiURL;

    public RestApiClient(String apiURL, String apiKey) {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        this.client = ObjectRegistry.getInstance(Client.class);
    }

    public ErrorObject upsertDetails(String propertyName, List<Map<String, Object>> details) {
        ErrorObject errorObject = null;
        try {
            Response response = invokeRestAPI(propertyName, details);
            log.info("Response status {}", response.getStatus());
            if (!ResponseUtils.is2xx(response)) {
                errorObject = response.readEntity(ErrorObject.class);
            }
        } catch (Exception e) {
            log.error(String.format("Custom API upsert failed for %s %s", this.apiURL,this.apiKey), e);
            errorObject = new ErrorObject("UNCLASSIFIED", e.getMessage());
        }
        return errorObject;
    }

    private Response invokeRestAPI(String propertyName, List<Map<String, Object>> inputDetails) {
        return this.client.target(apiURL)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(RestUtils.AUTHORIZATION_HEADER, BEARER_AUTHENTICATION + apiKey)
                .post(Entity.json(constructPayload(propertyName, inputDetails)));
    }

    private Object constructPayload(String propertyName, List<Map<String, Object>> inputDetails) {
        if (propertyName != null) {
            Map<String,Object> jsonObject = new HashMap<>();
            jsonObject.put(propertyName, inputDetails);
            return jsonObject;
        }
        return inputDetails;
    }
}
