package io.castled.events.warehousevents;

import io.castled.events.CastledEventType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WarehouseCreatedEvent extends WarehouseEvent {

    public WarehouseCreatedEvent(Long warehouseId){
        super(warehouseId, CastledEventType.WAREHOUSE_CREATED);
    }
}
