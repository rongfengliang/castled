package io.castled.apps.connectors.restapi;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppConnector;
import io.castled.apps.ExternalAppType;
import io.castled.apps.connectors.mixpanel.MixpanelAppSyncConfig;
import io.castled.apps.connectors.mixpanel.MixpanelObject;
import io.castled.apps.connectors.mixpanel.MixpanelObjectFields;
import io.castled.apps.dtos.AppSyncConfigDTO;
import io.castled.apps.models.ExternalAppSchema;
import io.castled.apps.models.GenericSyncObject;
import io.castled.apps.models.PrimaryKeyEligibles;
import io.castled.commons.models.AppSyncMode;
import io.castled.dtos.PipelineConfigDTO;
import io.castled.forms.dtos.FormFieldOption;
import io.castled.models.FieldMapping;
import io.castled.schema.models.FieldSchema;
import io.castled.schema.models.RecordSchema;
import org.apache.commons.collections.CollectionUtils;

import javax.ws.rs.BadRequestException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class RestApiAppConnector implements ExternalAppConnector<RestApiAppConfig,
        RestApiDataSink, RestApiAppSyncConfig> {

    @Override
    public List<FormFieldOption> getAllObjects(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        return Arrays.stream(RestApiObject.values()).map(restApiObject -> new FormFieldOption(new GenericSyncObject(restApiObject.getName(),
                ExternalAppType.RESTAPI), restApiObject.getName())).collect(Collectors.toList());
    }

    @Override
    public RestApiDataSink getDataSink() {
        return ObjectRegistry.getInstance(RestApiDataSink.class);
    }

    @Override
    public ExternalAppSchema getSchema(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        return new ExternalAppSchema(null, Lists.newArrayList());
    }

    public List<AppSyncMode> getSyncModes(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        String object = restApiAppSyncConfig.getObject().getObjectName();
        if(RestApiObject.POST.getName().equalsIgnoreCase(object)) {
            return Lists.newArrayList(AppSyncMode.UPSERT);
        }
        return Lists.newArrayList(AppSyncMode.INSERT,AppSyncMode.UPSERT,AppSyncMode.UPDATE);
    }

    public Class<RestApiAppSyncConfig> getMappingConfigType() {
        return RestApiAppSyncConfig.class;
    }

    @Override
    public Class<RestApiAppConfig> getAppConfigType() {
        return RestApiAppConfig.class;
    }


    public PipelineConfigDTO validateAndEnrichPipelineConfig(PipelineConfigDTO pipelineConfig) throws BadRequestException {
/*        RestApiAppSyncConfig mixpanelAppSyncConfig = (RestApiAppSyncConfig) pipelineConfig.getAppSyncConfig();
        String objectName = pipelineConfig.getAppSyncConfig().getObject().getObjectName();

        if(RestApiObject.POST.getName().equalsIgnoreCase(objectName)) {
            enrichPipelineConfigForUserProfileObject(pipelineConfig, mixpanelAppSyncConfig);
        }*/
        return pipelineConfig;
    }

    private void enrichPipelineConfigForUserProfileObject(PipelineConfigDTO pipelineConfig, RestApiAppSyncConfig mixpanelAppSyncConfig) throws BadRequestException{

        String distinctID = Optional.ofNullable(mixpanelAppSyncConfig.getDistinctID()).orElseThrow(()->new BadRequestException("Column uniquely identifying the User is mandatory"));

        List<FieldMapping> additionalMapping = Lists.newArrayList();
        Optional.ofNullable(distinctID).ifPresent((ID) -> additionalMapping.add(new FieldMapping(ID, MixpanelObjectFields.USER_PROFILE_FIELDS.DISTINCT_ID.getFieldName(),false)));
        pipelineConfig.getMapping().addAdditionalMappings(additionalMapping);
        pipelineConfig.getMapping().setPrimaryKeys(Collections.singletonList(MixpanelObjectFields.USER_PROFILE_FIELDS.DISTINCT_ID.getFieldName()));
    }

    public RecordSchema enrichWarehouseASchema(AppSyncConfigDTO appSyncConfigDTO , RecordSchema warehouseSchema) {


        return warehouseSchema;
    }

    private List<String> getAllReservedFieldsForEventProfile(RestApiAppSyncConfig mixpanelAppSyncConfig ){
        List<String> reservedFields = Lists.newArrayList();
        CollectionUtils.addIgnoreNull(reservedFields,mixpanelAppSyncConfig.getDistinctID());
        return reservedFields;
    }

}
