package com.finanzen.api.adapters.in.transaction.dto;

import java.math.BigDecimal;
import com.finanzen.api.domain.transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object for transaction creation.
 * <p>
 * Ensures business integrity at the API boundary, validating that
 * descriptions are present, amounts are positive, and types are valid.
 * </p>
 */
public record TransactionCreateDto(
        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Transaction type is required (INCOME or EXPENSE)")
        TransactionType type
) {}