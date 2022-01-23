package io.castled;

import com.google.inject.Inject;
import io.castled.daos.InstallationDAO;
import io.castled.events.CastledEventsClient;
import io.castled.events.CastledEventsConsumer;
import io.castled.events.NewInstallationEvent;
import io.castled.events.pipelineevents.PipelineEventConsumer;
import io.castled.kafka.consumer.ConsumerUtils;
import io.castled.pubsub.PubSubConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

@Slf4j
public class CastledAppManager {

    private final PipelineEventConsumer pipelineEventConsumer;
    private final CastledEventsConsumer castledEventsConsumer;
    private final PubSubConsumer pubSubConsumer;
    private final CastledEventsClient castledEventsClient;
    private final InstallationDAO installationDAO;


    @Inject
    public CastledAppManager(PipelineEventConsumer pipelineEventConsumer,
                             CastledEventsConsumer castledEventsConsumer,
                             PubSubConsumer pubSubConsumer, CastledEventsClient castledEventsClient,
                             Jdbi jdbi) {
        this.pipelineEventConsumer = pipelineEventConsumer;
        this.pubSubConsumer = pubSubConsumer;
        this.castledEventsConsumer = castledEventsConsumer;
        this.castledEventsClient = castledEventsClient;
        this.installationDAO = jdbi.onDemand(InstallationDAO.class);
    }

    public void initializeAppComponents() {
        ConsumerUtils.runKafkaConsumer(1, "pipeline_events", pipelineEventConsumer);
        ConsumerUtils.runKafkaConsumer(1, "castled_events", castledEventsConsumer);
        ConsumerUtils.runKafkaConsumer(1, "pubsub", pubSubConsumer);
    }
}
