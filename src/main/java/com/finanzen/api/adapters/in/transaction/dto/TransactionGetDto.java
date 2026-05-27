package com.finanzen.api.adapters.in.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finanzen.api.domain.transaction.TransactionType;

public record TransactionGetDto(
    Long id,
    String description,
    BigDecimal amount,
    TransactionType type,

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime createdAt

) {
}
