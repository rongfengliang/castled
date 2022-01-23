package io.castled.events;


import com.google.inject.Inject;
import io.castled.constants.KafkaApplicationConstants;
import io.castled.events.pipelineevents.PipelineEvent;
import io.castled.events.pipelineevents.PipelineEventType;
import io.castled.events.pipelineevents.PipelineEventsHandler;
import io.castled.kafka.KafkaApplicationConfig;
import io.castled.kafka.consumer.BaseKafkaConsumer;
import io.castled.kafka.consumer.KafkaConsumerConfiguration;
import io.castled.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;

@Slf4j
public class CastledEventsConsumer extends BaseKafkaConsumer {

    private final Map<CastledEventType, CastledEventsHandler> eventHandlers;

    @Inject
    public CastledEventsConsumer(KafkaApplicationConfig kafkaApplicationConfig,
                                 Map<CastledEventType, CastledEventsHandler> eventHandlers) {
        super(KafkaConsumerConfiguration.builder().bootstrapServers(kafkaApplicationConfig.getBootstrapServers())
                .consumerGroup(KafkaApplicationConstants.CASTLED_EVENTS_CONSUMER_GRP).topic(KafkaApplicationConstants.CASTLED_EVENTS_TOPIC)
                .retryOnUnhandledFailures(false).build());
        this.eventHandlers = eventHandlers;
    }

    @Override
    public long processRecords(List<ConsumerRecord<byte[], byte[]>> consumerRecords) throws Exception {
        long offset = -1;
        for (ConsumerRecord<byte[], byte[]> consumerRecord : consumerRecords) {
            try {
                CastledEvent castledEvent = JsonUtils.byteArrayToObject(consumerRecord.value(), CastledEvent.class);
                this.eventHandlers.get(castledEvent.getEventType()).handleCastledEvent(castledEvent);
                offset = consumerRecord.offset();
            } catch (Exception e) {
                log.error("Castled event consumption failed", e);
                throw e;
            }
        }
        return offset;
    }
}
