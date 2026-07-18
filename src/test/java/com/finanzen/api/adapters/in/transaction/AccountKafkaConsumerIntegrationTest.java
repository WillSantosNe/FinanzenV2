package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.BaseIntegrationTest;
import com.finanzen.api.adapters.out.account.AccountRepository;
import com.finanzen.api.adapters.out.account.JpaAccountRepository;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.account.AccountType;
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

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EmbeddedKafka(partitions = 1, topics = {"transaction-events"})
@ExtendWith(OutputCaptureExtension.class)
//@Transactional
@DisplayName("Kafka Consumer - transaction-events")
public class AccountKafkaConsumerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AccountRepository repository;

    @Test
    @DisplayName("Should consume transaction-events event and trigger email simulation logs")
    void shouldConsumeEventAndLogEmailNotification(CapturedOutput output) {
        // Arrange

        Account testAccount = new Account(
                null,
                "1234",
                new BigDecimal("3500.00"),
                AccountType.CHECKING,
                "teste3@gmail.com"
        );

        Account accountSaved = repository.save(testAccount);


        String transactionJson = """
                {
                    "id": 999,
                    "description": "Teclado Mecanico",
                    "amount": 350.00 ,
                    "type": "EXPENSE",
                    "userEmail": "teste3@gmail.com",
                    "accountId": %d
                }
                """.formatted(accountSaved.getId());

        // Act
        kafkaTemplate.send("transaction-events", "999", transactionJson);

        // Assert
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertTrue(output.getOut().contains("[UPDATE ACCOUNT BALANCE SERVICE]"));
                });
    }
}
