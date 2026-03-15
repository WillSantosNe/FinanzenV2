package com.finanzen.api.dto;

import java.math.BigDecimal;

import com.finanzen.api.model.TransactionType;

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
