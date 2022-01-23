package io.castled.events.appevents;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.CastledAppManager;
import io.castled.CastledStateStore;
import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppService;
import io.castled.apps.ExternalAppType;
import io.castled.events.CastledEvent;
import io.castled.events.CastledEventsHandler;
import io.castled.tracking.EventTags;
import io.castled.tracking.EventsTrackingClient;
import io.castled.tracking.TrackingEvent;
import io.castled.tracking.TrackingEventType;

@Singleton
public class AppCreateEventsHandler implements CastledEventsHandler {

    private final EventsTrackingClient eventsTrackingClient;
    private final ExternalAppService externalAppService;

    @Inject
    public AppCreateEventsHandler(EventsTrackingClient eventsTrackingClient, ExternalAppService externalAppService) {
        this.eventsTrackingClient = eventsTrackingClient;
        this.externalAppService = externalAppService;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        ExternalAppCreatedEvent externalAppCreatedEvent = (ExternalAppCreatedEvent) castledEvent;
        ExternalAppType externalAppType = this.externalAppService.getExternalApp
                (externalAppCreatedEvent.getExternalAppId(), true).getType();

        this.eventsTrackingClient.trackEvent(new TrackingEvent(TrackingEventType.APP_CREATED,
                CastledStateStore.installId, ImmutableMap.of(EventTags.APP_TYPE, externalAppType.toString())));

    }
}
