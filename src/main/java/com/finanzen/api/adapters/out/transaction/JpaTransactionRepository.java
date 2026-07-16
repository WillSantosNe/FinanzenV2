package com.finanzen.api.adapters.out.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Infrastructure Repository for {@link JpaTransactionEntity}.
 * <p>
 * Provides native database access through Spring Data abstractions. This interface
 * should only be injected into the specific outbound persistence adapters,
 * never into application services or domain use cases.
 * </p>
 */
@Repository
public interface JpaTransactionRepository extends JpaRepository<JpaTransactionEntity, Long> {

    /**
     * Custom database query to find all records associated with a specific user email.
     *
     * @param userEmail the owner's email address.
     * @param pageable  the pagination information.
     * @return a {@link Page} of database entities.
     */
    Page<JpaTransactionEntity> findAllByUserEmail(String userEmail, Pageable pageable);

    boolean existsByAccountIdAndAmountAndDescriptionAndTypeAndCreatedAtGreaterThanEqual(
            Long accountId,
            java.math.BigDecimal amount,
            String description,
            com.finanzen.api.domain.transaction.TransactionType type,
            java.time.LocalDateTime limitTime
    );
}