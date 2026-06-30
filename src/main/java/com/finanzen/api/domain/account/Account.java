package com.finanzen.api.domain.account;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String  accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private String userEmail;

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
}
