package com.finanzen.api.adapters.out.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.finanzen.api.domain.transaction.TransactionType;
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

/**
 * Infrastructure Database Entity for Transaction persistence (JPA/Hibernate).
 * <p>
 * This class serves exclusively as the Object-Relational Mapping (ORM) definition
 * for the "transactions" table. It isolated persistence-specific configurations,
 * column structures, and annotations away from the core Domain layer.
 * </p>
 */
@Entity(name = "Transaction")
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "user_email")
    private String userEmail;
}