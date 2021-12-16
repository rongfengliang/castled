package io.castled.apps.connectors.restapi;

import io.castled.apps.DataSink;
import io.castled.apps.connectors.mixpanel.*;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;
import io.castled.exceptions.CastledRuntimeException;
import io.castled.schema.models.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RestApiDataSink implements DataSink {

    private volatile RestApiObjectSink<Message> restApiObjectSink;

    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {

        this.restApiObjectSink = getObjectSink(dataSinkRequest);
        log.info("Sync started for mix panel");
        Message message;
        while ((message = dataSinkRequest.getMessageInputStream().readMessage()) != null) {
            this.restApiObjectSink.writeRecord(message);
        }
        this.restApiObjectSink.flushRecords();
    }

    private RestApiObjectSink<Message> getObjectSink(DataSinkRequest dataSinkRequest) {
        RestApiObjectSink<Message> bufferedObjectSink = null;
        RestApiObject restApiObject = RestApiObject
                .getObjectByName(dataSinkRequest.getAppSyncConfig().getObject().getObjectName());
        switch (restApiObject) {
            case POST:
                bufferedObjectSink = new RestApiPostCallSink(dataSinkRequest);
                break;
            default:
                throw new CastledRuntimeException(String.format("Invalid object type %s!", restApiObject.getName()));
        }
        return bufferedObjectSink;
    }

    @Override
    public AppSyncStats getSyncStats() {
        return Optional.ofNullable(this.restApiObjectSink)
                .map(audienceSinkRef -> this.restApiObjectSink.getSyncStats())
                .map(statsRef -> new AppSyncStats(statsRef.getRecordsProcessed(), statsRef.getOffset(), 0))
                .orElse(new AppSyncStats(0, 0, 0));
    }
}
