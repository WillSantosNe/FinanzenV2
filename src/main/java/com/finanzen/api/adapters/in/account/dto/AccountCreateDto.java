package com.finanzen.api.adapters.in.account.dto;

import com.finanzen.api.domain.account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountCreateDto(
        @NotBlank(message = "Account number cannot be blank")
        String  accountNumber,

        @NotNull(message = "Balance is required")
        @Positive(message = "Balance must be greater than zero")
        BigDecimal balance,

        @NotNull(message = "Transaction type is required (CHECKING or SAVINGS)")
        AccountType accountType
) {}
