package io.castled.events.appevents;

import io.castled.events.CastledEvent;
import io.castled.events.CastledEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ExternalAppEvent extends CastledEvent {

    private Long externalAppId;

    public ExternalAppEvent(Long externalAppId, CastledEventType castledEventType) {
        super(castledEventType);
        this.externalAppId = externalAppId;
    }

    @Override
    public String entityId() {
        return String.valueOf(externalAppId);
    }
}
