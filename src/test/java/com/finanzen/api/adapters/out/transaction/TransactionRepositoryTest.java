package com.finanzen.api.adapters.out.transaction;

import com.finanzen.api.AbstractDatabaseTest;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TransactionRepository.class)
public class TransactionRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_save_and_find_transaction_successfully() {
        // Arrange
        Transaction transactionToSave = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                null,
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L);

        // Act
        Transaction savedTransaction = transactionRepository.save(transactionToSave);
        entityManager.flush(); // Forcando o banco de dados a dar o insert
        entityManager.clear(); // Limpando a memoria do hibernate

        Optional<Transaction> foundTransaction = transactionRepository.findById(savedTransaction.getId());

        // Assert
        assertTrue(foundTransaction.isPresent());
        assertEquals(savedTransaction.getId(), foundTransaction.get().getId());
        assertEquals(savedTransaction.getDescription(), foundTransaction.get().getDescription());
        assertEquals(savedTransaction.getAmount(), foundTransaction.get().getAmount());
        assertEquals(savedTransaction.getCreatedAt(), foundTransaction.get().getCreatedAt());
        assertEquals(savedTransaction.getUserEmail(), foundTransaction.get().getUserEmail());
        assertEquals(savedTransaction.getAccountId(), foundTransaction.get().getAccountId());
    }

    @Test
    public void should_return_empty_optional_when_transaction_id_does_not_exist() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Optional<Transaction> foundTransaction = transactionRepository.findById(nonExistentId);

        // Assert
        assertTrue(foundTransaction.isEmpty());
    }


    @Test
    public void should_return_true_when_duplicate_recent_transaction_exists() {
        // Arrange
        Transaction transaction1 = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L);

        Transaction transaction2 = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L);

        // Act
        Transaction savedTransaction1 = transactionRepository.save(transaction1);
        entityManager.flush();
        entityManager.clear();
        Transaction savedTransaction2 = transactionRepository.save(transaction2);
        entityManager.flush();
        entityManager.clear();

        boolean result = transactionRepository.existsDuplicateRecentTransaction(
                1L,
                savedTransaction2.getAmount(),
                savedTransaction2.getDescription(),
                savedTransaction2.getType(),
                LocalDateTime.now().minusSeconds(5)
        );

        // Assert
        assertTrue(result);
    }
}
