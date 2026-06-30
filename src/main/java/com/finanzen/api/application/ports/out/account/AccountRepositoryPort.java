package com.finanzen.api.application.ports.out.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.account.Account;

import java.util.Optional;

/**
 * Outbound Port representing the database persistence contract for the {@link Account} domain.
 * It abstracts the infrastructure framework (JPA/Hibernate) from the core application layer.
 */
public interface AccountRepositoryPort {

    /**
     * Persists or updates an account in the database.
     *
     * @param account the pure domain object to be saved.
     * @return the saved {@link Account} domain object with its database state.
     */
    Account save(Account account);

    /**
     * Retrieves an account by its unique database identifier.
     *
     * @param id the unique identifier of the account.
     * @return an {@link Optional} containing the domain object if found, or empty if not.
     */
    Optional<Account> findById(Long id);

    /**
     * ADMIN ONLY: Retrieves a paginated list of all accounts registered in the system.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @return a {@link PageResult} containing the paginated accounts.
     */
    PageResult<Account> findAllSystemWide(int page, int size);

    /**
     * USER: Retrieves a paginated list of accounts belonging to a specific owner email.
     *
     * @param userEmail the email used to filter account ownership.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @return a {@link PageResult} containing the user's accounts.
     */
    PageResult<Account> findAllByUserEmail(String userEmail, int page, int size);

    /**
     * Removes an account permanently from the database.
     *
     * @param id the unique identifier of the account to be deleted.
     */
    void deleteById(Long id);

    /**
     * Retrieves a account by their account number.
     *
     * @param accountNumber the account number to search for.
     * @return an {@link Optional} containing the account if found, or empty otherwise.
     */
    Optional<Account> findByAccountNumber(String accountNumber);
}