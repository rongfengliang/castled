package io.castled.events.appevents;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.CastledStateStore;
import io.castled.apps.ExternalAppService;
import io.castled.apps.ExternalAppType;
import io.castled.events.CastledEvent;
import io.castled.events.CastledEventsHandler;
import io.castled.tracking.EventTags;
import io.castled.tracking.InstallTrackingClient;
import io.castled.tracking.InstallTrackingEvent;
import io.castled.tracking.TrackingEventType;

@Singleton
public class AppCreateEventsHandler implements CastledEventsHandler {

    private final InstallTrackingClient installTrackingClient;
    private final ExternalAppService externalAppService;

    @Inject
    public AppCreateEventsHandler(InstallTrackingClient installTrackingClient, ExternalAppService externalAppService) {
        this.installTrackingClient = installTrackingClient;
        this.externalAppService = externalAppService;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        ExternalAppCreatedEvent externalAppCreatedEvent = (ExternalAppCreatedEvent) castledEvent;
        ExternalAppType externalAppType = this.externalAppService.getExternalApp
                (externalAppCreatedEvent.getExternalAppId(), true).getType();

        this.installTrackingClient.trackEvent(new InstallTrackingEvent(TrackingEventType.APP_CREATED,
                CastledStateStore.installId, ImmutableMap.of(EventTags.APP_TYPE, externalAppType.toString())));

    }
}
