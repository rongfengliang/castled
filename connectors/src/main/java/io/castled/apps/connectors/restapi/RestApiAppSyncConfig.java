package io.castled.apps.connectors.restapi;

import io.castled.OptionsReferences;
import io.castled.apps.syncconfigs.AppSyncConfig;
import io.castled.commons.models.AppSyncMode;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

import static io.castled.forms.FormGroups.TUNNEL_GROUP;

@Getter
@Setter
@GroupActivator(dependencies = {"bulk"}, group = "bulk")
public class RestApiAppSyncConfig extends AppSyncConfig {


    @FormField(required = false, type = FormFieldType.TEXT_BOX, title = "Parallelism", description = "Parallelism")
    private Integer parallelism;

    @FormField(required = false, description = "Bulk update", title = "Enable bulk", schema = FormFieldSchema.BOOLEAN, type = FormFieldType.CHECK_BOX)
    private boolean bulk;

    @FormField(required = false, type = FormFieldType.TEXT_BOX, title = "Json Array path", placeholder = "parent.child.subchild", description = "Json Array Path", group = "bulk")
    private Integer jsonPath;

    @FormField(required = false, type = FormFieldType.TEXT_BOX, title = "Batch Size", description = "Batch Size", group = "bulk")
    private Integer batchSize;
}
