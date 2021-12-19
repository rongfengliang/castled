package io.castled.apps.connectors.restapi;

import io.castled.apps.DataSink;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.AppSyncStats;
import io.castled.schema.models.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RestApiDataSink implements DataSink {

    private volatile RestApiObjectSink<Message> restApiObjectSink;

    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {
        this.restApiObjectSink = getObjectSink(dataSinkRequest);
        log.info("Sync started for REST API");
        Message message;
        while ((message = dataSinkRequest.getMessageInputStream().readMessage()) != null) {
            this.restApiObjectSink.writeRecord(message);
        }
        this.restApiObjectSink.flushRecords();
    }

    private RestApiObjectSink<Message> getObjectSink(DataSinkRequest dataSinkRequest) {
        Integer noOfThreads = Optional.ofNullable(((RestApiAppSyncConfig) dataSinkRequest.getAppSyncConfig()).getParallelThreads()).orElse(0);
        Integer batchSize = Optional.ofNullable(((RestApiAppSyncConfig) dataSinkRequest.getAppSyncConfig()).getBatchSize()).orElse(0);
        if (batchSize > 0 && noOfThreads > 0) {
            return new RestApiBufferedParallelSink(dataSinkRequest);
        }
        return new RestApiObjectBufferedSink(dataSinkRequest);
    }

    @Override
    public AppSyncStats getSyncStats() {
        return Optional.ofNullable(this.restApiObjectSink)
                .map(audienceSinkRef -> this.restApiObjectSink.getSyncStats())
                .map(statsRef -> new AppSyncStats(statsRef.getRecordsProcessed(), statsRef.getOffset(), 0))
                .orElse(new AppSyncStats(0, 0, 0));
    }
}
