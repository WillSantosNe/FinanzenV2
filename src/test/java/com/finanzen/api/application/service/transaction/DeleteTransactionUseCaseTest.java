package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
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
@DisplayName("Application: Delete Transaction Use Case Tests")
public class DeleteTransactionUseCaseTest {

    @Mock
    private TransactionRepositoryPort repository;

    @Mock
    private FindTransactionByIdPort findTransactionByIdPort;

    @InjectMocks
    private DeleteTransactionUseCase useCase;

    @Test
    @DisplayName("Should successfully delete transaction when the ID exists in the system")
    void shouldDeleteTransactionSuccessfully(){
        // Arrange
        Long idDelete = 1L;
        Transaction mockedTransaction = mock(Transaction.class);
        when(findTransactionByIdPort.findById(idDelete)).thenReturn(mockedTransaction);

        // Act
        useCase.delete(idDelete);

        // Assert
        verify(findTransactionByIdPort, times(1)).findById(idDelete);
        verify(repository, times(1)).deleteById(idDelete);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException when transaction ID is not found")
    void shouldThrowTransactionNotFoundExceptionDelete(){
        // Arrange
        Long idDelete = 1L;

        when(findTransactionByIdPort.findById(idDelete)).thenThrow(
                new TransactionNotFoundException("Transaction not found")
        );

        // Act & Assert
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
            () -> useCase.delete(idDelete)
        );

        assertEquals("Transaction not found", exception.getMessage());   // Verificando se a mensagem é igual

        verify(findTransactionByIdPort, times(1)).findById(idDelete);
        verify(repository, never()).deleteById(idDelete);
    }
}
