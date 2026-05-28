package com.finanzen.api.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Pure domain model representing a financial transaction in the system.
 * <p>
 * This class encapsulates the core business rules and state for a Transaction.
 * It is completely isolated from external frameworks, annotations, and
 * infrastructure details, strictly adhering to Hexagonal Architecture principles.
 * </p>
 * * <b>Business Rules:</b>
 * <ul>
 * <li>The transaction amount must always be strictly greater than zero.</li>
 * </ul>
 */
public class Transaction {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private TransactionType type;
    private String userEmail;

    /**
     * Constructs a new Transaction and enforces initial business invariants.
     *
     * @param id          the unique identifier of the transaction (null if not yet persisted).
     * @param description a brief explanation of the transaction.
     * @param amount      the monetary value of the transaction (must be positive).
     * @param createdAt   the exact timestamp when the transaction occurred or was registered.
     * @param type        the classification of the transaction (e.g., INCOME, EXPENSE).
     * @param userEmail   the email of the user who owns this transaction.
     * @throws IllegalArgumentException if the provided amount is zero or negative.
     */
    public Transaction(Long id, String description, BigDecimal amount, LocalDateTime createdAt, TransactionType type, String userEmail) {
        // Regra de Negócio Pura e Isolada!
        if (amount != null && amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero.");
        }

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.type = type;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Evaluates equality based strictly on the unique identifier (ID).
     * <p>
     * Two transactions are considered equal if they have the same non-null ID,
     * regardless of their current mutable state (description, amount, etc.).
     * </p>
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}