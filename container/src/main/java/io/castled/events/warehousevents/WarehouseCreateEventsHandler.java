package io.castled.events.warehousevents;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.CastledStateStore;
import io.castled.events.CastledEvent;
import io.castled.events.CastledEventsHandler;
import io.castled.tracking.EventTags;
import io.castled.tracking.EventsTrackingClient;
import io.castled.tracking.TrackingEvent;
import io.castled.tracking.TrackingEventType;
import io.castled.warehouses.WarehouseService;
import io.castled.warehouses.WarehouseType;

@Singleton
public class WarehouseCreateEventsHandler implements CastledEventsHandler {

    private final EventsTrackingClient eventsTrackingClient;
    private final WarehouseService warehouseService;

    @Inject
    public WarehouseCreateEventsHandler(EventsTrackingClient eventsTrackingClient,
                                        WarehouseService warehouseService) {
        this.eventsTrackingClient = eventsTrackingClient;
        this.warehouseService = warehouseService;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        WarehouseCreatedEvent warehouseCreateEvent = (WarehouseCreatedEvent) castledEvent;
        WarehouseType warehouseType = this.warehouseService.getWarehouse(warehouseCreateEvent.getWarehouseId(), true).getType();

        this.eventsTrackingClient.trackEvent(new TrackingEvent
                (TrackingEventType.WAREHOUSE_CREATED, CastledStateStore.installId, ImmutableMap.of(EventTags.WAREHOUSE_TYPE, warehouseType.toString())));

    }
}
