package com.finanzen.api.application.service.account;


import com.finanzen.api.application.exceptions.BusinessException;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.account.AccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Update Account Balance Use Case Tests")
public class UpdateAccountBalanceUseCaseTest {

    @Mock
    private AccountRepositoryPort repositoryPort;

    @Mock
    private FindAccountByIdPort findAccountByIdPort;

    @InjectMocks
    private UpdateAccountBalanceUseCase useCase;

    @Test
    @DisplayName("Should successfully apply credit delta to account balance")
    void shouldApplyCreditDeltaSuccessfully() {
        // Arrange
        Long accountId = 1L;
        String email = "william@gmail.com";
        BigDecimal creditDelta = new BigDecimal("150.00");

        Account existingAccount = new Account(accountId, "123456", new BigDecimal("500.00"), AccountType.CHECKING, email);

        when(findAccountByIdPort.findById(accountId,email)).thenReturn(existingAccount);
        when(repositoryPort.save(existingAccount)).thenReturn(existingAccount);

        // Act
        Account result = useCase.execute(accountId, creditDelta, email);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("650.00"), existingAccount.getBalance());
        verify(repositoryPort, times(1)).save(existingAccount);
        verify(findAccountByIdPort, times(1)).findById(accountId,email);
    }

    @Test
    @DisplayName("Should throw BusinessException when debit delta exceeds balance")
    void shouldThrowExceptionWhenFundsAreInsufficient() {
        // Arrange
        Long accountId = 1L;
        String email = "william@gmail.com";
        BigDecimal expensiveDebitDelta = new BigDecimal("-1000.00"); // Tentando tirar 1000 de quem só tem 500

        Account existingAccount = new Account(accountId, "123456", new BigDecimal("500.00"), AccountType.CHECKING, email);

        when(findAccountByIdPort.findById(accountId,email)).thenReturn(existingAccount);

        // Act & Assert
        BusinessException businessException = assertThrows(
                BusinessException.class,() ->  useCase.execute(accountId, expensiveDebitDelta, email));

        // Repositório nunca foi chamado para salvar uma conta inválida
        verify(repositoryPort, never()).save(any(Account.class));
        verify(findAccountByIdPort, times(1)).findById(accountId,email);

    }

    @Test
    @DisplayName("Should successfully apply debit delta when balance reaches exactly zero")
    void shouldDebitExactBalanceAmountWithSuccess() {
        // Arrange
        Long accountId = 1L;
        String email = "william@gmail.com";
        BigDecimal exactDebitDelta = new BigDecimal("-500.00"); // Tirando tudo

        Account existingAccount = new Account(accountId, "123456", new BigDecimal("500.00"), AccountType.CHECKING, email);

        when(findAccountByIdPort.findById(accountId,email)).thenReturn(existingAccount);
        when(repositoryPort.save(existingAccount)).thenReturn(existingAccount);

        // Act
        Account result = useCase.execute(accountId, exactDebitDelta, email);

        // Assert
        assertNotNull(result);
        assertEquals(0, existingAccount.getBalance().compareTo(BigDecimal.ZERO));

        verify(repositoryPort, times(1)).save(existingAccount);
    }
}