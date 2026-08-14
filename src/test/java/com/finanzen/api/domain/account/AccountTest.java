package com.finanzen.api.domain.account;

import com.finanzen.api.domain.exceptions.DomainRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the pure domain entity {@link Account}.
 * <p>
 * Ensures that core financial mathematics and overdraft protections
 * are strictly enforced without relying on external frameworks.
 * </p>
 */
class AccountTest {

    @Test
    void should_increase_balance_when_delta_is_positive() {
        // Arrange
        Account account = new Account(1L, "123", new BigDecimal("100.00"), AccountType.CHECKING, "email@gmail.com");

        // Act
        account.applyDelta(new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), account.getBalance());
    }

    @Test
    void should_decrease_balance_when_delta_is_negative_and_funds_are_sufficient() {
        // Arrange
        Account account = new Account(1L, "123", new BigDecimal("100.00"), AccountType.CHECKING, "email@gmail.com");

        // Act
        account.applyDelta(new BigDecimal("-50.00"));

        // Assert
        assertEquals(new BigDecimal("50.00"), account.getBalance());
    }

    @Test
    void should_throw_domain_rule_exception_when_delta_is_negative_and_funds_are_insufficient() {
        // Arrange
        Account account = new Account(1L, "123", new BigDecimal("100.00"), AccountType.CHECKING, "email@gmail.com");

        // Act & Assert
        DomainRuleException exception = assertThrows(
                DomainRuleException.class, () -> account.applyDelta(new BigDecimal("-150.00")));

        assertEquals("Insufficient funds for this operation", exception.getMessage());
    }
}