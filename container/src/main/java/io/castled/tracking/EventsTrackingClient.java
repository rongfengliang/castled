package io.castled.tracking;

import com.google.inject.Inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class EventsTrackingClient {

    private final Client client;

    @Inject
    public EventsTrackingClient(Client client) {
        this.client = client;
    }

    public void trackEvent(TrackingEvent trackingEvent) {
        this.client.target("https://app.castled.io/backend/tracking")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(trackingEvent));
    }

}
