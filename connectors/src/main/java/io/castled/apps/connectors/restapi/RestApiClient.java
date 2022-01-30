package io.castled.apps.connectors.restapi;

import io.castled.ObjectRegistry;
import io.castled.models.RestMethod;
import io.castled.models.TargetTemplateMapping;
import io.castled.utils.MustacheUtils;
import io.castled.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Slf4j
public class RestApiClient {

    public static final String CONTENT_TYPE = "Content-Type";
    private final Client client;
    private final String payloadTemplate;
    private final String apiURL;
    private final Map<String, String> headers;
    private final RestMethod restMethod;

    public RestApiClient(TargetTemplateMapping targetTemplateMapping) {
        this.apiURL = targetTemplateMapping.getUrl();
        this.payloadTemplate = targetTemplateMapping.getTemplate();
        this.headers = targetTemplateMapping.getHeaders();
        this.client = ObjectRegistry.getInstance(Client.class);
        this.restMethod = targetTemplateMapping.getMethod();
    }

    public ErrorObject upsertDetails(List<Map<String, Object>> details) {
        ErrorObject errorObject = null;
        try {
            Response response = invokeRestAPI(details);
            log.info("Response status {}", response.getStatus());
            if (!ResponseUtils.is2xx(response)) {
                errorObject = response.readEntity(ErrorObject.class);
            }
        } catch (Exception e) {
            log.error(String.format("Custom API upsert failed for %s %s", this.apiURL, this.payloadTemplate), e);
            errorObject = new ErrorObject("UNCLASSIFIED", e.getMessage());
        }
        return errorObject;
    }

    private Response invokeRestAPI(List<Map<String, Object>> inputDetails) {

        Invocation.Builder builder = this.client.target(apiURL)
                .request(headers.get(CONTENT_TYPE));
        headers.remove(CONTENT_TYPE);

        headers.entrySet().forEach(entry -> builder.header(entry.getKey(), entry.getValue()));

        if (RestMethod.PUT == restMethod) {
            return builder.put(Entity.json(constructPayload(inputDetails)));
        }
        return builder.post(Entity.json(constructPayload(inputDetails)));
    }

    private Object constructPayload(List<Map<String, Object>> inputDetails) {
        return MustacheUtils.constructPayload(payloadTemplate, inputDetails.get(0));
    }

}
