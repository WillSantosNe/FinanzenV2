package com.finanzen.api.infrastructure.dto;

import java.math.BigDecimal;

import com.finanzen.api.domain.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionCreateDto(
    /*
       NotBlank - usado para Strings garantindo que seja preenchido com valor. 
       @NotNull - usado para numeros, enums e datas garantindo que não venha nulo.
       @Positive - garante que um número seja maior que zero.
    */


    @NotBlank
    String description,

    @NotNull
    @Positive
    BigDecimal amount,

    @NotNull    
    TransactionType type
) {}
