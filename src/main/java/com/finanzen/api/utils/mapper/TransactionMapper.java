package com.finanzen.api.utils.mapper;

import com.finanzen.api.adapters.out.transaction.JpaTransactionEntity;
import com.finanzen.api.domain.transaction.Transaction;

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
    public static Transaction toDomain(JpaTransactionEntity entity) {
        if (entity == null)
            return null;

        return new Transaction(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getCreatedAt(),
                entity.getType(),
                entity.getUserEmail(),
                entity.getAccountId()
        );
    }

    /**
     * Converts a pure Domain object into a JPA entity (Infrastructure) for
     * persistence.
     *
     * @param domain the pure domain object.
     * @return the corresponding {@link JpaTransactionEntity} ready to be persisted, or
     * null if the input is null.
     */
    public static JpaTransactionEntity toEntity(Transaction domain) {
        if (domain == null)
            return null;

        return new JpaTransactionEntity(
                domain.getId(),
                domain.getDescription(),
                domain.getAmount(),
                domain.getCreatedAt(),
                domain.getType(),
                domain.getUserEmail(),
                domain.getAccountId()
        );
    }
}
