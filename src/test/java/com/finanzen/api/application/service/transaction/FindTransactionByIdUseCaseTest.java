package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FindTransactionByIdUseCaseTest {

    @InjectMocks
    private FindTransactionByIdUseCase findTransactionByIdUseCase;

    @Mock
    private TransactionRepositoryPort repository;

    @Test
    public void should_return_transaction_when_id_is_valid(){
        // Arrange
        Long rawId = 1L;
        String rawEmail = "email@gmail.com";

        Transaction transactionFound = new Transaction(
                rawId,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                rawEmail,
                1L);

        BDDMockito.given(repository.findById(rawId)).willReturn(Optional.of(transactionFound));

        // Act
        Transaction transactionResult = findTransactionByIdUseCase.findById(rawId, rawEmail);

        // Assert
        assertEquals(rawId, transactionResult.getId());
        assertEquals(new BigDecimal("100.00"), transactionResult.getAmount());
        assertEquals(rawEmail, transactionResult.getUserEmail());
    }

    @Test
    public void should_throw_transaction_not_found_exception_when_id_does_not_exist(){
        // Arrange
        Long rawId = 1L;
        String rawEmail = "email@gmail.com";
        BDDMockito.given(repository.findById(rawId)).willReturn(Optional.empty());

        // Act & Assert
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> findTransactionByIdUseCase.findById(rawId, rawEmail));

        assertEquals("Transaction with the id " + rawId + " not found in the system", exception.getMessage());
    }

}
