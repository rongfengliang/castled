package io.castled.apps.connectors.restapi;

import io.castled.apps.syncconfigs.AppSyncConfig;
import io.castled.forms.FormField;
import io.castled.forms.FormFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiAppSyncConfig extends AppSyncConfig {

    @FormField(required = false, type = FormFieldType.TEXT_BOX, title = "Property Name in the input object", description = "Batch Size")
    private String propertyName;

    @FormField(type = FormFieldType.TEXT_BOX, title = "Batch Size", description = "Batch Size")
    private Integer batchSize;

    @FormField(type = FormFieldType.TEXT_BOX, title = "Parallel Invocation Count", description = "Parallel Invocation Count")
    private Integer parallelThreads;
}
