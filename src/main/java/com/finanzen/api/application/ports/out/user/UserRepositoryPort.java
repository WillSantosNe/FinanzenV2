package com.finanzen.api.application.ports.out.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.user.User;

import java.util.Optional;

public interface UserRepositoryPort {

    /**
     * Saves or updates a domain object in the underlying storage.
     *
     * @param user the domain entity to be persisted.
     * @return the {@link User} with its persisted state (e.g., generated ID).
     */
    User save(User user);

    /**
     * Retrieves a domain object by its unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return an {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<User> findById(Long id);

    /**
     * ADMIN ONLY: Retrieves a paginated list of all users from the storage.
     */
    PageResult<User> findAllSystemWide(int page, int size);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
