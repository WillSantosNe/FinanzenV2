package com.finanzen.api.adapters.out.messaging.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.application.ports.out.outbox.OutboxRepositoryPort;
import com.finanzen.api.domain.outbox.OutboxEvent;
import com.finanzen.api.utils.mapper.OutboxMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxRelayWorker {

    private final  OutboxRepositoryPort outboxRepositoryPort;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "5000")
    public void processPedingEvents(){
        List<OutboxEvent> pendingEvents = outboxRepositoryPort.findUnprocessedEvents();

        for (OutboxEvent outboxEvent : pendingEvents) {
            try {
                kafkaTemplate.send("transaction-events",
                        outboxEvent.getAggregateId().toString(),
                        outboxEvent.getPayload());

                outboxEvent.setProcessed(true);
                outboxRepositoryPort.save(outboxEvent);

            }catch (Exception e){
                System.out.println("Failed to relay outbox event ID: " + outboxEvent.getId() + " - " + e.getMessage());
            }
        }
    }

}
