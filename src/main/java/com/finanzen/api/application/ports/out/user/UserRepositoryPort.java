package com.finanzen.api.application.ports.out.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.user.User;

import java.util.Optional;

/**
 * Outbound Port (Driven Port) for User persistence.
 * <p>
 * This interface defines the contract that the core application layer requires
 * to store and retrieve user data. It strictly handles pure domain objects
 * ({@link User}), ensuring that business logic remains agnostic to database
 * schema details or persistence frameworks (like JPA or Hibernate).
 * </p>
 */
public interface UserRepositoryPort {

    /**
     * Saves a new user or updates an existing one in the underlying storage.
     *
     * @param user the pure domain entity to be persisted.
     * @return the {@link User} with its persisted state (e.g., generated ID).
     */
    User save(User user);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<User> findById(Long id);

    /**
     * Retrieves a paginated list of all users from the storage.
     *
     * @param page the zero-based index of the page to retrieve.
     * @param size the maximum number of items per page.
     * @return a pure {@link PageResult} containing the requested {@link User} objects.
     */
    PageResult<User> findAllSystemWide(int page, int size);

    /**
     * Checks if a user already exists for the given email.
     *
     * @param email the email address to check.
     * @return true if the email is in use, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email to search for.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<User> findByEmail(String email);
}