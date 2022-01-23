package io.castled.events;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.tracking.InstallTrackingClient;
import io.castled.tracking.InstallTrackingEvent;
import io.castled.tracking.TrackingEventType;

@Singleton
public class NewInstallationEventsHandler implements CastledEventsHandler {

    private final InstallTrackingClient installTrackingClient;

    @Inject
    public NewInstallationEventsHandler(InstallTrackingClient installTrackingClient) {
        this.installTrackingClient = installTrackingClient;
    }

    @Override
    public void handleCastledEvent(CastledEvent castledEvent) {
        NewInstallationEvent newInstallationEvent = (NewInstallationEvent) castledEvent;
        this.installTrackingClient.trackEvent(new InstallTrackingEvent
                (TrackingEventType.NEW_INSTALLATION, newInstallationEvent.getInstallationId(), Maps.newHashMap()));
    }
}
