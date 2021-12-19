package io.castled.apps.connectors.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestMetadata {
    private String httpMethod;
    private String apiKey;
    private String apiURL;
}
