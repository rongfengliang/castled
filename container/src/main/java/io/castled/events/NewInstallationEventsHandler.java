package io.castled.events;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.tracking.EventsTrackingClient;
import io.castled.tracking.TrackingEvent;
import io.castled.tracking.TrackingEventType;

@Singleton
public class NewInstallationEventsHandler implements CastledEventsHandler {

    private final EventsTrackingClient eventsTrackingClient;

    @Inject
    public NewInstallationEventsHandler(EventsTrackingClient eventsTrackingClient) {
        this.eventsTrackingClient = eventsTrackingClient;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        NewInstallationEvent newInstallationEvent = (NewInstallationEvent) castledEvent;
        this.eventsTrackingClient.trackEvent(new TrackingEvent
                (TrackingEventType.NEW_INSTALLATION, newInstallationEvent.getInstallationId(), Maps.newHashMap()));
    }
}
