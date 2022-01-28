package io.castled.apps.connectors.restapi;

import io.castled.OptionsReferences;
import io.castled.apps.syncconfigs.AppSyncConfig;
import io.castled.commons.models.AppSyncMode;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiAppSyncConfig extends AppSyncConfig {

    @FormField(required = false,type = FormFieldType.TEXT_BOX, title = "Batch Size", description = "Batch Size")
    private Integer batchSize;

    @FormField(required = false,type = FormFieldType.TEXT_BOX, title = "Parallel Invocation Count", description = "Parallel Invocation Count")
    private Integer parallelThreads;

}
