package io.castled.tracking;

import com.google.inject.Inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class InstallTrackingClient {

    private final Client client;

    @Inject
    public InstallTrackingClient(Client client) {
        this.client = client;
    }

    public void trackEvent(InstallTrackingEvent trackingEvent) {
        this.client.target("https://app.castled.io/backend/v1/tracking/installs")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(trackingEvent));
    }

}
