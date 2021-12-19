package io.castled.apps.connectors.restapi;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppConnector;
import io.castled.apps.ExternalAppType;
import io.castled.apps.dtos.AppSyncConfigDTO;
import io.castled.apps.models.ExternalAppSchema;
import io.castled.apps.models.GenericSyncObject;
import io.castled.commons.models.AppSyncMode;
import io.castled.dtos.PipelineConfigDTO;
import io.castled.forms.dtos.FormFieldOption;
import io.castled.models.FieldMapping;
import io.castled.schema.models.RecordSchema;

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
        if(RestApiObject.FLAT_STRUCTURED_OBJECT.getName().equalsIgnoreCase(object)) {
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
        RestApiAppSyncConfig restApiAppSyncConfig = (RestApiAppSyncConfig) pipelineConfig.getAppSyncConfig();
        String objectName = pipelineConfig.getAppSyncConfig().getObject().getObjectName();
        if(RestApiObject.FLAT_STRUCTURED_OBJECT.getName().equalsIgnoreCase(objectName)) {
            enrichPipelineConfigForGenericInputObject(pipelineConfig, restApiAppSyncConfig);
        }
        return pipelineConfig;
    }

    private void enrichPipelineConfigForGenericInputObject(PipelineConfigDTO pipelineConfig, RestApiAppSyncConfig restApiAppSyncConfig) throws BadRequestException{
        String pkIdentifier = Optional.ofNullable(restApiAppSyncConfig.getDistinctID()).orElseThrow(()->new BadRequestException("Column uniquely identifying the User is mandatory"));
        List<FieldMapping> additionalMapping = Lists.newArrayList();
        Optional.ofNullable(pkIdentifier).ifPresent((ID) -> additionalMapping.add(new FieldMapping(ID, CustomeAPIObjectFields.GENERIC_OBJECT_FIELD.IDENITIFIER.getFieldName(),false)));
        pipelineConfig.getMapping().addAdditionalMappings(additionalMapping);
        pipelineConfig.getMapping().setPrimaryKeys(Collections.singletonList(CustomeAPIObjectFields.GENERIC_OBJECT_FIELD.IDENITIFIER.getFieldName()));
    }

    public RecordSchema enrichWarehouseASchema(AppSyncConfigDTO appSyncConfigDTO , RecordSchema warehouseSchema) {
        return warehouseSchema;
    }

}
