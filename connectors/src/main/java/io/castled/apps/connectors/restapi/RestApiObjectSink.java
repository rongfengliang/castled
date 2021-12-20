package io.castled.apps.connectors.restapi;

import io.castled.apps.BufferedObjectSink;
import io.castled.commons.models.MessageSyncStats;


public abstract class RestApiObjectSink<Message> extends BufferedObjectSink<Message> {
    public abstract MessageSyncStats getSyncStats();
}
