package io.castled.apps.connectors.googlesheets;

import io.castled.OptionsReferences;
import io.castled.apps.models.GenericSyncObject;
import io.castled.apps.syncconfigs.AppSyncConfig;
import io.castled.commons.models.AppSyncMode;
import io.castled.forms.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupActivator(dependencies = {"object"}, group = MappingFormGroups.SYNC_MODE)
public class GoogleSheetsAppSyncConfig extends AppSyncConfig {

    @FormField(title = "Select the sheet to sync", type = FormFieldType.DROP_DOWN, group = MappingFormGroups.OBJECT, optionsRef = @OptionsRef(value = OptionsReferences.OBJECT, type = OptionsRefType.DYNAMIC))
    private GoogleSheetsSyncObject object;

    @FormField(description = "Clear Sheets before every run", title = "Clear Sheets before every run", schema = FormFieldSchema.BOOLEAN, type = FormFieldType.CHECK_BOX)
    private boolean clearSheets;

    @FormField(type = FormFieldType.RADIO_GROUP, title = "Sync Mode", description = "Sync mode which controls whether records will be appended, updated or upserted", group = MappingFormGroups.SYNC_MODE,
            optionsRef = @OptionsRef(value = OptionsReferences.SYNC_MODE, type = OptionsRefType.DYNAMIC))
    private AppSyncMode mode;


}
