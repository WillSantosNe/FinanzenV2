package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.in.account.UpdateAccountBalancePort;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.account.AccountType;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Create Transaction Use Case Tests")
public class CreateTransactionUseCaseTest {

    @Mock
    private TransactionRepositoryPort repository; // Cria duble de porta de saída

    @Mock
    private TransactionEventPublisherPort eventPublisher;

    @Mock
    private FindAccountByIdPort findAccountByIdPort;


    @InjectMocks
    private CreateTransactionUseCase createTransactionUseCase; // Injeta no duble no usecase

    private Transaction inputTransaction;

    @BeforeEach // Vai rodar antes de cada @Test
    void setUp(){
        inputTransaction = new Transaction(null, "Freelance Dev", new BigDecimal("3500.00"), null, TransactionType.INCOME, null, 1L);
    }

    @Test
    @DisplayName("Should successfully enrich transaction with metadata and persist it")
    void shouldCreateTransactionSuccessfully() {
        // Arrange
        String userEmail = "willi@gmail.com";
        Long accountId = 1L;

        Account mockAccount = new Account(
                accountId, "123456", new BigDecimal("1000.00"), AccountType.CHECKING, userEmail
        );

        Transaction mockSavedTransaction = new Transaction(
                99L,
                inputTransaction.getDescription(),
                inputTransaction.getAmount(),
                LocalDateTime.now(),
                inputTransaction.getType(),
                userEmail,
                accountId
        );

        when(findAccountByIdPort.findById(accountId, userEmail)).thenReturn(mockAccount);


        // mock devolve a transacao com ID
        when(repository.save(any(Transaction.class))).thenReturn(mockSavedTransaction);

        // Act
        Transaction result = createTransactionUseCase.create(inputTransaction, userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(userEmail, result.getUserEmail());
        assertNotNull(result.getCreatedAt());

        // Garantindo que as portas de integração e persistência foram acionadas
        verify(findAccountByIdPort, times(1)).findById(accountId, userEmail);
        verify(repository, times(1)).save(any(Transaction.class));
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           