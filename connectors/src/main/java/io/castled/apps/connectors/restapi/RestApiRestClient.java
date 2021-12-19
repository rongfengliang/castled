package io.castled.apps.connectors.restapi;

import com.sun.research.ws.wadl.HTTPMethods;
import io.castled.ObjectRegistry;
import io.castled.apps.connectors.mixpanel.MixpanelObjectFields;
import io.castled.apps.connectors.mixpanel.dto.*;
import io.castled.exceptions.CastledRuntimeException;
import io.castled.utils.ResponseUtils;
import io.castled.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class RestApiRestClient {

    public static final String BASIC_AUTH = "Basic ";
    private final Client client;
    private final String apiKey;
    private final String apiURL;

    public RestApiRestClient(String apiURL, String apiKey) {
        this.apiURL= apiURL;
        this.apiKey = apiKey;
        this.client = ObjectRegistry.getInstance(Client.class);
    }

    public List<ObjectAndError>  upsertDetails(List<Map<String,Object>> details) {

        List<ObjectAndError> objectAndErrors = new ArrayList<>();
        try {
            Response response = invokeRestAPI(details);
            log.info("Response status {}",response.getStatus());

            if(!ResponseUtils.is2xx(response)) {
                handleUpsertFailure(response,objectAndErrors,details);
            }
        }
        catch (BadRequestException badRequestException) {
            log.error("Custom API upsert failed ", badRequestException);
            objectAndErrors.addAll(details.stream().
                    map(object -> new ObjectAndError((String) object.get(CustomeAPIObjectFields.GENERIC_OBJECT_FIELD.IDENITIFIER.getFieldName()),Collections.singletonList(badRequestException.getMessage())))
                    .collect(Collectors.toList()));
        }
        catch (Exception e) {
            log.error("Upsert failed", e);
            throw new CastledRuntimeException(e);
        }

        return objectAndErrors;
    }

    private boolean handleUpsertFailure(Response response, List<ObjectAndError> objectAndErrors, List<Map<String, Object>> requestPayloadDetails) {
        objectAndErrors.addAll(requestPayloadDetails.stream().
                map(object -> new ObjectAndError((String) object.get(CustomeAPIObjectFields.GENERIC_OBJECT_FIELD.IDENITIFIER.getFieldName()),Collections.singletonList("Error code :"+response.getStatus())))
                .collect(Collectors.toList()));
        return true;
    }


    private Response invokeRestAPI(List<Map<String, Object>> inputDetails) {
        return this.client.target(apiURL)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(RestUtils.AUTHORIZATION_HEADER, "Bearer "+apiKey)
                .post(Entity.json(inputDetails));
    }
}
