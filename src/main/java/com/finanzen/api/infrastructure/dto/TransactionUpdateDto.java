package com.finanzen.api.infrastructure.dto;

import java.math.BigDecimal;

import com.finanzen.api.domain.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionUpdateDto(
    @NotBlank
    String description,

    @NotNull
    @Positive
    BigDecimal amount,

    @NotNull
    TransactionType type
) {}
