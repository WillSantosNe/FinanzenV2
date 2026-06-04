package com.finanzen.api.application.service.transaction;

import com.finanzen.api.adapters.out.transaction.TransactionRepository;
import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Find Transaction By Id Use Case Tests")
public class FindTransactionByIdUseCaseTest {
    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private FindTransactionByIdUseCase useCase;

    @Test
    @DisplayName("Should return Transaction by id successfully")
    void shouldReturnTransactionByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        Transaction mockedTransaction = new Transaction(
                id,
                "Lunch",
                new BigDecimal("30.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "user@gmail.com");

        when(repository.findById(id)).thenReturn(Optional.of(mockedTransaction));

        // Act
        Transaction transaction = useCase.findById(id);

        // Assert
        assertNotNull(transaction);
        assertEquals(mockedTransaction, transaction);
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException")
    void shouldThrowTransactionNotFoundException() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> useCase.findById(id));
        verify(repository, times(1)).findById(id);
    }
}
