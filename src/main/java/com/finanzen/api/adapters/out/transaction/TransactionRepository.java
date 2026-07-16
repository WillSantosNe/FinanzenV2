package com.finanzen.api.adapters.out.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.TransactionType;
import com.finanzen.api.utils.mapper.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.finanzen.api.domain.transaction.Transaction;

/**
 * Persistence Adapter (Outbound Adapter) for transactions.
 * <p>
 * This component acts as a bridge between the application's outbound port
 * ({@link TransactionRepositoryPort}) and the actual database infrastructure
 * (Spring Data JPA). It intercepts pure domain objects, translates them into
 * database entities using a mapper, performs the physical database operations,
 * and translates the results back to the domain model.
 * </p>
 */
@Component
public class TransactionRepository implements TransactionRepositoryPort {

    private final JpaTransactionRepository repository;

    public TransactionRepository(JpaTransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Maps a pure domain model to a database entity, persists it, and maps it back.
     * <p>
     * This method handles the dynamic translation between the core business entity
     * and the ORM entity, shielding the domain from direct knowledge of persistence state.
     * </p>
     *
     * @param transaction the pure domain entity to save.
     * @return the persisted domain object populated with its database-generated state (e.g., ID).
     */
    @Override
    public Transaction save(Transaction transaction) {
        JpaTransactionEntity entity = TransactionMapper.toEntity(transaction);
        JpaTransactionEntity savEntity = repository.save(entity);
        return TransactionMapper.toDomain(savEntity);
    }

    /**
     * Looks up a record by its database primary key and wraps it into a domain {@link Optional}.
     * <p>
     * If the record exists, it is lazily converted to the core domain representation.
     * Otherwise, an empty optional is returned to let the domain layer choose its error strategy.
     * </p>
     *
     * @param id the unique database identifier.
     * @return an Optional containing the mapped pure {@link Transaction}, or empty.
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id).map(TransactionMapper::toDomain);
    }

    /**
     * ADMIN ONLY: Retrieves all records system-wide, translating Spring Data
     * {@link Page} objects into framework-agnostic {@link PageResult} models.
     * <p>
     * Used exclusively for auditing and global administration management. It prevents the
     * leaking of Spring Data infrastructure abstractions into the application services.
     * </p>
     *
     * @param page zero-based page index.
     * @param size number of items per page.
     * @return a decoupled {@link PageResult} containing system-wide transactions.
     */
    @Override
    public PageResult<Transaction> findAllSystemWide(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaTransactionEntity> springPage = repository.findAll(pageable);

        List<Transaction> transactions = springPage.getContent().stream()
                .map(TransactionMapper::toDomain)
                .toList();

        return new PageResult<>(
                transactions,
                springPage.getNumber(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }

    /**
     * USER: Retrieves records matching a specific email, abstracting Spring Data pagination
     * behind a pure application layer model.
     * <p>
     * Filters results on the database level to ensure data isolation. The framework-dependent
     * page properties are unpacked and restructured into a custom domain-friendly page container.
     * </p>
     *
     * @param userEmail the email address acting as the ownership filter.
     * @param page      zero-based page index.
     * @param size      number of items per page.
     * @return a decoupled {@link PageResult} containing the user's specific transactions.
     */
    @Override
    public PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaTransactionEntity> springPage = repository.findAllByUserEmail(userEmail, pageable);

        List<Transaction> transactions = springPage.getContent().stream()
                .map(TransactionMapper::toDomain)
                .toList();

        return new PageResult<>(
                transactions,
                springPage.getNumber(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }

    /**
     * Delegates row deletion to the internal repository infrastructure.
     * <p>
     * Performs a hard delete on the database line associated with the given identifier.
     * </p>
     *
     * @param id the unique primary key of the record to remove.
     */
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsDuplicateRecentTransaction(Long accountId, BigDecimal amount, String description, TransactionType type, LocalDateTime limitTime) {
        return repository.existsByAccountIdAndAmountAndDescriptionAndTypeAndCreatedAtGreaterThanEqual(accountId, amount, description, type, limitTime);
    }
}