package com.finanzen.api.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finanzen.api.domain.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Transaction")
@Table(name = "transactions")
@Getter // Gera Getters
@Setter // Gera Setters
@NoArgsConstructor
@AllArgsConstructor // Facilita na criacao de objetos
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;

    @Column(name = "created_at") // snake_case no banco de dados
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "user_email") 
    private String userEmail;
}
