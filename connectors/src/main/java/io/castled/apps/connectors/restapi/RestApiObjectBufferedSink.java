package io.castled.apps.connectors.restapi;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import io.castled.ObjectRegistry;
import io.castled.apps.models.DataSinkRequest;
import io.castled.commons.models.MessageSyncStats;
import io.castled.commons.streams.ErrorOutputStream;
import io.castled.schema.SchemaUtils;
import io.castled.schema.models.Field;
import io.castled.schema.models.Message;
import io.castled.schema.models.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Singleton
@Slf4j
public class RestApiObjectBufferedSink extends RestApiObjectSink<Message> {

    private final RestApiRestClient restApiRestClient;
    private final RestApiErrorParser restApiErrorParser;
    private final ErrorOutputStream errorOutputStream;
    private final AtomicLong processedRecords = new AtomicLong(0);
    private final long batchSize;
    private final String payloadProperty;
    private long lastProcessedOffset = 100;

    public RestApiObjectBufferedSink(DataSinkRequest dataSinkRequest) {
        String apiURL = ((RestApiAppConfig) dataSinkRequest.getExternalApp().getConfig()).getApiURL();
        String apiKey = ((RestApiAppConfig) dataSinkRequest.getExternalApp().getConfig()).getApiKey();
        this.batchSize = Optional.ofNullable(((RestApiAppSyncConfig) dataSinkRequest.getAppSyncConfig()).getBatchSize()).orElse(1);
        this.restApiRestClient = new RestApiRestClient(apiURL, apiKey);
        this.errorOutputStream = dataSinkRequest.getErrorOutputStream();
        this.restApiErrorParser = ObjectRegistry.getInstance(RestApiErrorParser.class);
        payloadProperty = ((RestApiAppSyncConfig) dataSinkRequest.getAppSyncConfig()).getPropertyName();
    }

    @Override
    protected void writeRecords(List<Message> messages) {
        ErrorObject errorObject = this.restApiRestClient.upsertDetails(this.payloadProperty,
                messages.stream().map(Message::getRecord).map(this::constructProperties).collect(Collectors.toList()));

        Optional.ofNullable(errorObject).ifPresent((objectAndError1) -> messages.
                forEach(message -> this.errorOutputStream.writeFailedRecord(message, restApiErrorParser.getPipelineError(objectAndError1.getCode(), objectAndError1.getMessage()))));

        this.processedRecords.addAndGet(messages.size());
        this.lastProcessedOffset = Math.max(lastProcessedOffset, Iterables.getLast(messages).getOffset());
    }

    @Override
    public MessageSyncStats getSyncStats() {
        return new MessageSyncStats(processedRecords.get(), lastProcessedOffset);
    }

    @Override
    public long getMaxBufferedObjects() {
        return batchSize;
    }

    public void flushRecords() throws Exception {
        super.flushRecords();
    }

    private Map<String, Object> constructProperties(Tuple record) {
        Map<String, Object> recordProperties = Maps.newHashMap();
        for (Field field : record.getFields()) {
            Object value = record.getValue(field.getName());
            if (value != null) {
                if (SchemaUtils.isZonedTimestamp(field.getSchema())) {
                    recordProperties.put(field.getName(), ((ZonedDateTime) value).toEpochSecond());
                } else {
                    recordProperties.put(field.getName(), value);
                }
            }
        }
        return recordProperties;
    }
}
