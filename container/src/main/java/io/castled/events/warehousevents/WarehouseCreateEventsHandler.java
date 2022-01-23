package io.castled.events.warehousevents;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.CastledStateStore;
import io.castled.events.CastledEvent;
import io.castled.events.CastledEventsHandler;
import io.castled.tracking.EventTags;
import io.castled.tracking.InstallTrackingClient;
import io.castled.tracking.InstallTrackingEvent;
import io.castled.tracking.TrackingEventType;
import io.castled.warehouses.WarehouseService;
import io.castled.warehouses.WarehouseType;

@Singleton
public class WarehouseCreateEventsHandler implements CastledEventsHandler {

    private final InstallTrackingClient installTrackingClient;
    private final WarehouseService warehouseService;

    @Inject
    public WarehouseCreateEventsHandler(InstallTrackingClient installTrackingClient,
                                        WarehouseService warehouseService) {
        this.installTrackingClient = installTrackingClient;
        this.warehouseService = warehouseService;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        WarehouseCreatedEvent warehouseCreateEvent = (WarehouseCreatedEvent) castledEvent;
        WarehouseType warehouseType = this.warehouseService.getWarehouse(warehouseCreateEvent.getWarehouseId(), true).getType();

        this.installTrackingClient.trackEvent(new InstallTrackingEvent
                (TrackingEventType.WAREHOUSE_CREATED, CastledStateStore.installId, ImmutableMap.of(EventTags.WAREHOUSE_TYPE, warehouseType.toString())));

    }
}
