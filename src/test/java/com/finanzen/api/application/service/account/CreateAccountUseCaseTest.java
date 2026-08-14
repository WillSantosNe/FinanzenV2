package com.finanzen.api.application.service.account;

import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.account.AccountType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @InjectMocks
    private CreateAccountUseCase createAccountUseCase;

    @Mock private AccountRepositoryPort repository;

    @Test
    public void should_create_account_with_zero_balance_successfully(){
        // Arrange
        // Simulando que vem do mundo exterior um balance diferente de zero
        Account rawAccount = new Account(null, "123", new BigDecimal("100.00"), AccountType.CHECKING, "email@gmail.com");
        Account accountToSave = new Account(null, "123", BigDecimal.ZERO, AccountType.CHECKING, "email@gmail.com");

        BDDMockito.given(repository.existsByAccountNumber(rawAccount.getAccountNumber())).willReturn(false);
        BDDMockito.given(repository.save(any(Account.class))).willReturn(accountToSave);

        // Act
        Account savedAccount = createAccountUseCase.create(rawAccount, "email@gmail.com");

        // Assert
        assertEquals(BigDecimal.ZERO, savedAccount.getBalance());
        assertEquals("123",  savedAccount.getAccountNumber());
    }

}
