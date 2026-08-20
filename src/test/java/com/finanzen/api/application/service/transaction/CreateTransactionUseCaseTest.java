package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.AccountNotFoundException;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.account.AccountType;
import com.finanzen.api.domain.exceptions.DomainRuleException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;

// Importe suas portas e entidades reais aqui...

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUseCaseTest {

    @InjectMocks
    private CreateTransactionUseCase createTransactionUseCase;

    @Mock private FindAccountByIdPort findAccountByIdPort;
    @Mock private TransactionRepositoryPort transactionRepositoryPort;
    @Mock private TransactionEventPublisherPort transactionEventPublisherPort;
    @Mock private AccountRepositoryPort accountRepositoryPort;

    @Test
    public void should_create_income_transaction_update_account_and_publish_event_successfully() {
        // Arrange
        Account account = new Account(
                1L,
                "1234",
                new BigDecimal("1000.00"),
                AccountType.CHECKING,
                "email@gmail.com");

        Transaction rawTransaction = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                null,
                TransactionType.INCOME,
                "email@gmail.com",
                1L);

        Transaction savedTransaction = new Transaction(
                1L,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.INCOME,
                "email@gmail.com",
                1L
        );

        BDDMockito.given(transactionRepositoryPort.existsDuplicateRecentTransaction(
                any(),any(),any(),any(),any())).willReturn(false);

        BDDMockito.given(findAccountByIdPort.findById(anyLong(), any())).willReturn(account);
        BDDMockito.given(accountRepositoryPort.save(account)).willReturn(account);
        BDDMockito.given(transactionRepositoryPort.save(rawTransaction)).willReturn(savedTransaction);


        // Act
        Transaction transactionResult = createTransactionUseCase.create(rawTransaction, "email@gmail.com");

        // Assert
        assertEquals(1L, transactionResult.getId());
        assertEquals("email@gmail.com", transactionResult.getUserEmail());

        BDDMockito.then(transactionEventPublisherPort).should().publishTransactionCreated(savedTransaction);
        BDDMockito.then(accountRepositoryPort).should().save(account);
        BDDMockito.then(transactionRepositoryPort).should().save(rawTransaction);

        assertEquals(new BigDecimal("1100.00"), account.getBalance());

    }

    @Test
    public void should_create_expense_transaction_update_account_and_publish_event_successfully() {
        // Arrange
        Account account = new Account(
                1L,
                "1234",
                new BigDecimal("1000.00"),
                AccountType.CHECKING,
                "email@gmail.com");

        Transaction rawTransaction = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                null,
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L);

        Transaction savedTransaction = new Transaction(
                1L,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L
        );


        BDDMockito.given(transactionRepositoryPort.existsDuplicateRecentTransaction(
                any(),any(),any(),any(),any())).willReturn(false);

        BDDMockito.given(findAccountByIdPort.findById(anyLong(), any())).willReturn(account);
        BDDMockito.given(accountRepositoryPort.save(account)).willReturn(account);
        BDDMockito.given(transactionRepositoryPort.save(rawTransaction)).willReturn(savedTransaction);


        // Act
        Transaction transactionResult = createTransactionUseCase.create(rawTransaction, "email@gmail.com");

        // Assert
        assertEquals(1L, transactionResult.getId());
        assertEquals("email@gmail.com", transactionResult.getUserEmail());

        BDDMockito.then(transactionEventPublisherPort).should().publishTransactionCreated(savedTransaction);
        BDDMockito.then(accountRepositoryPort).should().save(account);
        BDDMockito.then(transactionRepositoryPort).should().save(rawTransaction);

        assertEquals(new BigDecimal("900.00"), account.getBalance());

    }

    @Test
    public void should_throw_account_not_found_exception_when_sender_account_is_invalid() {
        // Arrange
        Long accountId = 1L;
        Transaction  rawTransaction = new Transaction(null,
                "description",
                new BigDecimal("100.00"),
                null,
                TransactionType.EXPENSE,
                "email@gmail.com",
                accountId);

        BDDMockito.given(transactionRepositoryPort.existsDuplicateRecentTransaction(
                any(),any(),any(),any(),any())).willReturn(false);

        BDDMockito.given(findAccountByIdPort.findById(anyLong(), any())).willThrow(
                new AccountNotFoundException("Account with id: " + accountId + " not found"));

        // Act & Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> createTransactionUseCase.create(rawTransaction, "email@gmail.com"));

        assertEquals("Account with id: " + accountId + " not found", exception.getMessage());

        BDDMockito.then(accountRepositoryPort).should(never()).save(any(Account.class));
        BDDMockito.then(transactionRepositoryPort).should(never()).save(any(Transaction.class));
        BDDMockito.then(transactionEventPublisherPort).should(never()).publishTransactionCreated(any(Transaction.class));
    }

    @Test
    public void should_allow_domain_rule_exception_to_propagate_when_account_rejects_transaction() {
        // Arrange
        Account account = new Account(
                1L,
                "1234",
                new BigDecimal("50.00"),
                AccountType.CHECKING,
                "email@gmail.com");

        Transaction rawTransaction = new Transaction(
                null,
                "description",
                new BigDecimal("100.00"),
                null,
                TransactionType.EXPENSE,
                "email@gmail.com",
                1L);

        BDDMockito.given(transactionRepositoryPort.existsDuplicateRecentTransaction(
                any(),any(),any(),any(),any())).willReturn(false);

        BDDMockito.given(findAccountByIdPort.findById(anyLong(), any())).willReturn(account);


        // Act & Assert
        DomainRuleException exception = assertThrows(
                DomainRuleException.class, () -> createTransactionUseCase.create(rawTransaction, "email@gmail.com"));

        assertEquals("Insufficient funds for this operation", exception.getMessage());

        BDDMockito.then(accountRepositoryPort).should(never()).save(any(Account.class));
        BDDMockito.then(transactionRepositoryPort).should(never()).save(any(Transaction.class));
        BDDMockito.then(transactionEventPublisherPort).should(never()).publishTransactionCreated(any(Transaction.class));
    }
}