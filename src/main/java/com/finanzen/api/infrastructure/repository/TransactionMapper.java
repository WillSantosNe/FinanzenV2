package com.finanzen.api.infrastructure.repository;

import com.finanzen.api.domain.Transaction;

/**
 * Utility class for mapping between Domain objects and Infrastructure Entities.
 * <p>
 * This mapper acts as a translator, ensuring that the core domain logic
 * never leaks into the database layer, and database constraints never
 * pollute the pure business model.
 * </p>
 */
public class TransactionMapper {

    /**
     * Converts a JPA entity (Infrastructure) into a pure Domain object.
     *
     * @param entity the JPA entity retrieved from the database.
     * @return the corresponding {@link Transaction} domain object, or null if the
     * input is null.
     */
    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null)
            return null;

        return new Transaction(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getCreatedAt(),
                entity.getType(),
                entity.getUserEmail() 
        );
    }

    /**
     * Converts a pure Domain object into a JPA entity (Infrastructure) for
     * persistence.
     *
     * @param domain the pure domain object.
     * @return the corresponding {@link TransactionEntity} ready to be persisted, or
     * null if the input is null.
     */
    public static TransactionEntity toEntity(Transaction domain) {
        if (domain == null)
            return null;

        return new TransactionEntity(
                domain.getId(),
                domain.getDescription(),
                domain.getAmount(),
                domain.getCreatedAt(),
                domain.getType(),
                domain.getUserEmail() 
        );
    }
}
