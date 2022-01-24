package io.castled.events.pipelineevents;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.castled.CastledStateStore;
import io.castled.apps.ExternalAppService;
import io.castled.apps.ExternalAppType;
import io.castled.misc.PipelineScheduleManager;
import io.castled.models.Pipeline;
import io.castled.services.PipelineService;
import io.castled.tracking.EventTags;
import io.castled.tracking.InstallTrackingClient;
import io.castled.tracking.InstallTrackingEvent;
import io.castled.tracking.TrackingEventType;
import io.castled.warehouses.WarehouseService;
import io.castled.warehouses.WarehouseType;

public class PipelineCreateEventsHandler implements PipelineEventsHandler {

    private final PipelineScheduleManager pipelineScheduleManager;
    private final InstallTrackingClient installTrackingClient;
    private final PipelineService pipelineService;
    private final WarehouseService warehouseService;
    private final ExternalAppService externalAppService;


    @Inject
    public PipelineCreateEventsHandler(PipelineScheduleManager pipelineScheduleManager,
                                       InstallTrackingClient installTrackingClient, PipelineService pipelineService,
                                       WarehouseService warehouseService, ExternalAppService externalAppService) {
        this.pipelineScheduleManager = pipelineScheduleManager;
        this.installTrackingClient = installTrackingClient;
        this.pipelineService = pipelineService;
        this.warehouseService = warehouseService;
        this.externalAppService = externalAppService;
    }

    @Override
    public void handlePipelineEvent(PipelineEvent pipelineEvent) {
        this.pipelineScheduleManager.reschedulePipeline(pipelineEvent.getPipelineId());
        Pipeline pipeline = this.pipelineService.getActivePipeline(pipelineEvent.getPipelineId(), true);
        if (pipeline == null) {
            return;
        }
        WarehouseType warehouseType = this.warehouseService.getWarehouse(pipeline.getWarehouseId(), true).getType();
        ExternalAppType externalAppType = this.externalAppService.getExternalApp(pipeline.getAppId(), true).getType();

        this.installTrackingClient.trackEvent(new InstallTrackingEvent(TrackingEventType.PIPELINE_CREATED, CastledStateStore.installId,
                ImmutableMap.of(EventTags.WAREHOUSE_TYPE, warehouseType.toString(), EventTags.APP_TYPE, externalAppType.toString())));
    }
}
