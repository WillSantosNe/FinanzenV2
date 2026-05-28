package com.finanzen.api.application.ports.in.user;

import com.finanzen.api.domain.user.User;

/**
 * Inbound Port (Driving Port) for retrieving a specific user profile.
 * <p>
 * This interface defines the contract for looking up a user by their unique
 * identifier, providing a clean boundary for administrative or profile-based lookups.
 * </p>
 */
public interface FindUserByIdPort {

    /**
     * Executes the use case to find a user by their unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return the requested {@link User} pure domain object.
     * @throws com.finanzen.api.application.exceptions.UserNotFoundException if the ID does not exist.
     */
    User findById(Long id);
}