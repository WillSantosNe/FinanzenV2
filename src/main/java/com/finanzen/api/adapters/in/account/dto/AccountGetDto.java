package com.finanzen.api.adapters.in.account.dto;

import com.finanzen.api.domain.account.AccountType;

import java.math.BigDecimal;

public record AccountGetDto(
        Long id,
        String  accountNumber,
        BigDecimal balance,
        AccountType accountType,
        String userEmail
) {
}
