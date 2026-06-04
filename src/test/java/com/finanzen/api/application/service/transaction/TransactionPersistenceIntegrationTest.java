package com.finanzen.api.application.service.transaction;

import com.finanzen.api.BaseIntegrationTest;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Transactional // Limpa dos dados criados após a execução
@DisplayName("Integration: Transaction Database Persistence Tests")
public class TransactionPersistenceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TransactionRepositoryPort repository; // Repository conectado ao docker

    @Test
    @DisplayName("Should save a transaction in real PostgreSQL database and retrieve it by id")
    void shouldPersistAndRetrieveTransaction() {
        // Arrange
        Transaction transaction = new Transaction(
                null,
                "Monitor Gamer",
                new BigDecimal("1500.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "dev@finanzen.com"
        );

        // Act
        Transaction savedTransaction = repository.save(transaction);

        // Assert
        assertNotNull(savedTransaction.getId(), "Database should generate a real serial ID");

        // Buscando na base do container pelo id para garantir persistencia
        Transaction foundTransaction = repository.findById(savedTransaction.getId())
                .orElseThrow(() -> new AssertionError("Transaction was not found in the real database"));

        assertEquals("Monitor Gamer", foundTransaction.getDescription());
        assertEquals(new BigDecimal("1500.00"), foundTransaction.getAmount());
        assertEquals("dev@finanzen.com", foundTransaction.getUserEmail());
    }


}
