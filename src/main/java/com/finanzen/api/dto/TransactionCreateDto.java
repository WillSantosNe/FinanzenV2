package com.finanzen.api.dto;

import java.math.BigDecimal;

import com.finanzen.api.model.TransactionType;

public record TransactionCreateDto(
    String description,
    BigDecimal amount,
    TransactionType type
) {}
