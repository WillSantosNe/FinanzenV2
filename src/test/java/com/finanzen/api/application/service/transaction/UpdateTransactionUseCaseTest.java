package com.finanzen.api.application.service.transaction;

import com.finanzen.api.adapters.out.transaction.TransactionRepository;
import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Update Transaction Use Case Tests")
public class UpdateTransactionUseCaseTest {
    
    @Mock
    private TransactionRepository repository;

    @Mock
    private FindTransactionByIdPort findTransactionByIdPort;

    @InjectMocks
    private UpdateTransactionUseCase useCase;

    @Test
    @DisplayName("Should return Transaction update sucessfully")
    void shouldUpdateTransactionSucessfully(){
        // Arrange
        Long id = 1L;
        Transaction mockedTransaction = mock(Transaction.class);

        when(findTransactionByIdPort.findById(id)).thenReturn(mockedTransaction);

        // Act
        Transaction transaction = useCase.update(id, mockedTransaction);

        // Assert
        assertNotNull(transaction);
        assertEquals(mockedTransaction, transaction);
        verify(repository, times(1)).save(mockedTransaction);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException")
    void shouldThrowTransactionNotFoundException() {
        // Arrange
        Long id = 1L;
        Transaction mockedTransaction = mock(Transaction.class);

        when(findTransactionByIdPort.findById(id)).thenThrow(
                new TransactionNotFoundException("Transaction not found")
        );

        // Act & Assert
        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> useCase.update(id, mockedTransaction));

        assertEquals("Transaction not found", exception.getMessage());
        verify(findTransactionByIdPort, times(1)).findById(id);
        verify(repository, never()).save(mockedTransaction);
    }
}
