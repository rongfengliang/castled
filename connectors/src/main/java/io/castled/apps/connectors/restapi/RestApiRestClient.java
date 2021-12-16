package io.castled.apps.connectors.restapi;

import com.sun.research.ws.wadl.HTTPMethods;
import io.castled.ObjectRegistry;
import io.castled.apps.connectors.mixpanel.MixpanelObjectFields;
import io.castled.apps.connectors.mixpanel.dto.*;
import io.castled.exceptions.CastledRuntimeException;
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


    private String apiKey = null;
    private String apiURL = null;

    public static final String EVENT_URL = "https://api.mixpanel.com/import";
    public static final String USER_PROFILE_URL = "https://api.mixpanel.com/engage#profile-batch-update";
    public static final String GROUP_PROFILE_URL = "https://api.mixpanel.com/groups#group-batch-update";


    public RestApiRestClient(String apiURL, String apiKey) {
        this.apiURL= apiURL;
        this.apiKey = apiKey;
        this.client = ObjectRegistry.getInstance(Client.class);
    }

    public List<UserProfileAndError> upsertDetails(List<Map<String,Object>> details) {

        List<UserProfileAndError> userProfileAndErrors = new ArrayList<>();
        try {
            Response response = invokeRestAPI(details);
            log.info("Response status {}",response.getStatus());

            if(response.getStatus()!=200) {
                handleUpsertFailure(response,userProfileAndErrors,details);
            }
        }
        catch (BadRequestException badRequestException) {
            log.error("Userprofile bulk upsert failed ", badRequestException);
            userProfileAndErrors.addAll(details.stream().map(contact -> new UserProfileAndError((String) contact.get("$"+ MixpanelObjectFields.USER_PROFILE_FIELDS.DISTINCT_ID.getFieldName()),
                    Collections.singletonList(badRequestException.getMessage()))).collect(Collectors.toList()));
        }
        catch (Exception e) {
            log.error("Upsert failed", e);
            throw new CastledRuntimeException(e);
        }

        return userProfileAndErrors;
    }

    private boolean handleUpsertFailure(Response response, List<UserProfileAndError> groupProfileAndErrors, List<Map<String, Object>> groupProfileDetails) {
        groupProfileAndErrors.addAll(groupProfileDetails.stream().
                map(event -> new UserProfileAndError( (String) event.get("$"+MixpanelObjectFields.USER_PROFILE_FIELDS.DISTINCT_ID.getFieldName()), Collections.singletonList("Error code :"+response.getStatus())))
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
