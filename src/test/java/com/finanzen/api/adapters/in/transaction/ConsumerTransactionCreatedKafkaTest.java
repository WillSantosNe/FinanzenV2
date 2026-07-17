package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EmbeddedKafka(partitions = 1, topics = {"transaction-events"})
@ExtendWith(OutputCaptureExtension.class)
@Transactional
@DisplayName("Kafka Consumer - transaction-events")
public class ConsumerTransactionCreatedKafkaTest extends BaseIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    @DisplayName("Should consume transaction-events event and trigger email simulation logs")
    void shouldConsumeEventAndLogEmailNotification(CapturedOutput output) {
        // Arrange
        String transactionJson = """
                {
                    "id": 999,
                    "description": "Teclado Mecanico",
                    "amount": 350.00,
                    "type": "EXPENSE",
                    "userEmail": "william@teste.com"
                }
                """;

        // Act
        kafkaTemplate.send("transaction-events", "999", transactionJson);

        // Assert
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertTrue(output.getOut().contains("[EMAIL SERVICE] -> Preparing financial alert notification..."));
                    assertTrue(output.getOut().contains("Sending email to: william@teste.com"));
                    assertTrue(output.getOut().contains("[EMAIL SERVICE] -> Notification delivered successfully for Transaction ID: 999"));
                });
    }
}
