package com.finanzen.api.application.service.account;


import com.finanzen.api.application.exceptions.AccountNotFoundException;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountBalanceUseCaseTest {

    @InjectMocks
    private UpdateAccountBalanceUseCase updateAccountBalanceUseCase;

    @Mock private AccountRepositoryPort repositoryPort;
    @Mock private FindAccountByIdPort findAccountByIdPort;

    @Test
    public void should_update_balance_and_save_account_successfully(){
        // Arrange
        Account account = new Account(1L, "123", new BigDecimal("1000.00"), AccountType.CHECKING, "email@gmail.com");
        BDDMockito.given(findAccountByIdPort.findById(1L, "email@gmail.com")).willReturn(account);
        BDDMockito.given(repositoryPort.save(account)).willReturn(account);

        // Act
        Account updatedAccount = updateAccountBalanceUseCase.execute(1L, new BigDecimal("500"), "email@gmail.com");

        // Assert
        assertEquals(1L, updatedAccount.getId());
        assertEquals("email@gmail.com", updatedAccount.getUserEmail());
        assertEquals(new BigDecimal("1500.00"), updatedAccount.getBalance());
    }

    @Test
    public void should_throw_account_not_found_exception_when_updating_invalid_account(){
        // Arrange
        BDDMockito.given(findAccountByIdPort.findById(anyLong(), anyString()))
                .willThrow(new AccountNotFoundException("Account with id: 1 not found"));

        // Act + Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> updateAccountBalanceUseCase.execute(1L,new BigDecimal("1000.00"),"email@gmail.com"));

        assertEquals("Account with id: 1 not found", exception.getMessage());
        BDDMockito.then(repositoryPort).should(never()).save(any(Account.class));

    }

}
