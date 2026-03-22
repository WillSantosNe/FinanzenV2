package com.finanzen.api.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finanzen.api.application.port.out.TransactionRepositoryPort;
import com.finanzen.api.domain.Transaction;

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
@Component // Uso de @Component para o string criar a classe e injetar onde precisarem da
           // porta
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final TransactionRepository repository;

    public TransactionPersistenceAdapter(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Translates the domain object to an entity, saves it to the database,
     * and returns the persisted state as a domain object.
     *
     * @param transaction the pure domain object to be saved.
     * @return the saved {@link Transaction} domain object.
     */
    @Override
    public Transaction save(Transaction transaction) {
        // Converte dominio puro em entity
        TransactionEntity entity = TransactionMapper.toEntity(transaction);

        // Salva no repository a entity criada
        TransactionEntity savEntity = repository.save(entity);

        // Retorna o dominio puro convertido de uma entity
        return TransactionMapper.toDomain(savEntity);
    }

    /**
     * Queries the database for a transaction entity and translates it to a domain
     * object.
     *
     * @param id the transaction identifier.
     * @return an {@link Optional} containing the mapped domain object if found.
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id).map(TransactionMapper::toDomain);
    }

    /**
     * Retrieves a paginated list of entities from the database and maps them to
     * domain objects.
     *
     * @param pagination the pagination parameters.
     * @return a {@link Page} of mapped {@link Transaction} domain objects.
     */
    @Override
    public Page<Transaction> findAll(Pageable pagination) {
        return repository.findAll(pagination)
                .map(TransactionMapper::toDomain);
    }

    /**
     * Physically deletes a transaction from the underlying database.
     *
     * @param id the transaction identifier.
     */
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
