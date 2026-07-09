package com.finanzen.api.domain.account;

import com.finanzen.api.application.exceptions.BusinessException;

import java.math.BigDecimal;

/**
 * Pure Domain Entity representing a bank account within the core system.
 * <p>
 * This entity acts as an Aggregate Root, enforcing business invariants and protecting
 * its state from unauthorized mutations. It remains completely decoupled from any
 * framework or persistence annotations (JPA/Spring).
 * </p>
 */
public class Account {
    private Long id;
    private final String accountNumber;
    private BigDecimal balance;
    private final AccountType accountType;
    private final String userEmail;

    /**
     * Constructs a new Account ensuring initial corporate invariants.
     *
     * @param id            the unique persistent identifier (can be null for new accounts).
     * @param accountNumber the immutable unique bank identifier for the account.
     * @param balance       the initial balance, must be strictly greater than zero.
     * @param accountType   the category of the account (e.g., CHECKING, SAVINGS).
     * @param userEmail     the owner's email address acting as the multi-tenant key.
     * @throws IllegalArgumentException if the balance is null or less than or equal to zero.
     */
    public Account(Long id, String accountNumber, BigDecimal balance, AccountType accountType, String userEmail) {
        if (balance == null) {
            throw new IllegalArgumentException("The initial account balance cannot be null.");
        }
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The account balance must be greater than zero.");
        }

        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Mutates the account balance by applying a transactional delta.
     * <p>
     * This method handles both credits (positive delta) and debits (negative delta).
     * If the operation is a debit, it automatically verifies if the account possesses
     * sufficient funds to prevent overdraft, fulfilling the core banking requirement.
     * </p>
     *
     * @param amountDelta the value to be added to (or subtracted from) the balance.
     * @throws IllegalArgumentException if the provided delta is null.
     * @throws BusinessException        if the operation is a debit and funds are insufficient.
     */
    public void applyDelta(BigDecimal amountDelta) {
        if (amountDelta == null) {
            throw new IllegalArgumentException("Delta amount cannot be null");
        }

        // Delta negativo é para debitos
        if (amountDelta.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal absoluteDelta = amountDelta.abs();

            // Guard Clause: Overdraft prevention
            if (this.balance.compareTo(absoluteDelta) < 0) {
                throw new BusinessException("Insufficient funds for this operation");
            }
        }

        // Faz a mudança do saldo
        this.balance = this.balance.add(amountDelta);
    }
}