package com.finanzen.api.application.ports.in.user;

import com.finanzen.api.domain.user.User;

/**
 * Inbound Port (Driving Port) for user registration.
 * <p>
 * This interface defines the contract for registering a new user in the system.
 * It ensures that the application boundary receives a pure {@link User} domain
 * object, enforcing a strict separation between external DTOs and the core business model.
 * </p>
 */
public interface CreateUserPort {

    /**
     * Executes the use case to register a new user.
     *
     * @param user the pure domain object containing user details (email, password, etc.).
     * @return the created {@link User} domain object, including the generated unique identifier.
     * @throws com.finanzen.api.application.exceptions.DuplicateEmailException if the email is already registered.
     */
    User create(User user);
}