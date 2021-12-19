package io.castled.apps.connectors.restapi;

import io.castled.OptionsReferences;
import io.castled.apps.models.GenericSyncObject;
import io.castled.apps.syncconfigs.AppSyncConfig;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@GroupActivator(dependencies = {"object"}, group = MappingFormGroups.SYNC_MODE)
@GroupActivator(dependencies = {"object"}, condition = "object.objectName == 'FLAT'", group = "postGroup")
@Getter
@Setter
public class RestApiAppSyncConfig extends AppSyncConfig {

    @FormField(title = "Select API", type = FormFieldType.DROP_DOWN, group = MappingFormGroups.OBJECT,
            optionsRef = @OptionsRef(value = OptionsReferences.OBJECT, type = OptionsRefType.DYNAMIC))
    private GenericSyncObject object;

    @FormField(type = FormFieldType.DROP_DOWN, group ="postGroup", title = "Warehouse Column uniquely identifying a row", description = "How a source record will be uniquely identified",
            optionsRef = @OptionsRef(value = OptionsReferences.WAREHOUSE_COLUMNS, type = OptionsRefType.DYNAMIC))
    private String distinctID;

    @FormField(type = FormFieldType.TEXT_BOX, group ="postGroup", title = "Batch Size", description = "Batch Size")
    private String batchSize;

    @FormField(type = FormFieldType.TEXT_BOX, group ="postGroup", title = "Parallel Invocation Count", description = "Parallel Invocation Count")
    private String parallelThreads;


}
