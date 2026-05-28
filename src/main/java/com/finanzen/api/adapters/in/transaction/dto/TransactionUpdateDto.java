package com.finanzen.api.adapters.in.transaction.dto;

import java.math.BigDecimal;
import com.finanzen.api.domain.transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object for updating existing transaction information.
 */
public record TransactionUpdateDto(
        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Transaction type is required")
        TransactionType type
) {}