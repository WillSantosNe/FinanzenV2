package com.finanzen.api.domain.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: Transaction Entity Tests")
public class TransactionTest {

    @Test
    @DisplayName("Should create a valid transaction when all parameters are correct")
    void shouldCreateValidTransaction(){
        // Arrange - preparacao
        Long id = 1L;
        String description = "Monthly salary";
        BigDecimal amount = new BigDecimal("5000.00");
        LocalDateTime createdAt = LocalDateTime.now();
        TransactionType type = TransactionType.INCOME;
        String userEmail = "test@gmail.com";

        // Act - execucao
        Transaction transaction = new Transaction(id, description, amount, createdAt, type, userEmail);

        // Assert - verificacao
        assertNotNull(transaction);
        assertEquals(id, transaction.getId());
        assertEquals(description, transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
        assertEquals(createdAt, transaction.getCreatedAt());
        assertEquals(type, transaction.getType());
        assertEquals(userEmail, transaction.getUserEmail());

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when transaction amount is zero")
    void shouldThrowExceptionWhenAmountIsZero() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1L, "Lanche", zeroAmount, LocalDateTime.now(), TransactionType.EXPENSE, "willi@gmail.com");
        });

        // Garante que a mensagem de erro da exceção é exatamente a que definimos no domínio
        assertEquals("The transaction amount must be greater than zero.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when transaction amount is negative")
    void shouldThrowExceptionWhenAmountIsNegative() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-15.50");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1L, "Uber", negativeAmount, LocalDateTime.now(), TransactionType.EXPENSE, "willi@gmail.com");
        });

        assertEquals("The transaction amount must be greater than zero.", exception.getMessage());
    }

}
