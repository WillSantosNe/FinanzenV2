package com.finanzen.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finanzen.api.model.TransactionType;

public record TransactionGetDto (
    Long id,
    String description,
    BigDecimal amount,
    TransactionType type,
    LocalDateTime createdAt

){}
