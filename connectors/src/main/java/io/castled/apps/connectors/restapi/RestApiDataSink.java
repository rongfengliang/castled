package io.castled.apps.connectors.restapi;

import io.castled.apps.DataSink;
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
        Integer noOfThreads = null;
        Integer batchSize = null;
        String parallelThreads = ((RestApiAppSyncConfig)dataSinkRequest.getAppSyncConfig()).getParallelThreads();
        if(parallelThreads!=null) {
            noOfThreads = Integer.parseInt(parallelThreads);
        }

        String size = ((RestApiAppSyncConfig)dataSinkRequest.getAppSyncConfig()).getBatchSize();
        if(size!=null) {
            batchSize = Integer.parseInt(parallelThreads);
        }

        if(batchSize>0 && noOfThreads>0){
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
