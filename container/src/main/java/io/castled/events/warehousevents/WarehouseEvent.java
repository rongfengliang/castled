package io.castled.events.warehousevents;

import io.castled.events.CastledEvent;
import io.castled.events.CastledEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseEvent extends CastledEvent {

    private Long warehouseId;

    public WarehouseEvent(Long warehouseId, CastledEventType castledEventType) {
        super(castledEventType);
        this.warehouseId = warehouseId;
    }

    @Override
    public String entityId() {
        return String.valueOf(warehouseId);
    }
}
