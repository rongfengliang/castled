package io.castled.events.appevents;

import io.castled.events.CastledEventType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExternalAppCreatedEvent extends ExternalAppEvent {

    public ExternalAppCreatedEvent(Long appId) {
        super(appId, CastledEventType.APP_CREATED);
    }
}
