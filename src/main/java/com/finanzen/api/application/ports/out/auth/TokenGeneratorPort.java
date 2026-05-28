package com.finanzen.api.application.ports.out.auth;

import com.finanzen.api.domain.user.User;

/**
 * Outbound Port (Driven Port) for generating authentication tokens.
 * <p>
 * This interface abstracts the generation of security tokens (e.g., JWT).
 * It allows the business logic to request a token for a verified user
 * without knowing the details of the token's structure, signing algorithm,
 * or the underlying library being used.
 * </p>
 */
public interface TokenGeneratorPort {

    /**
     * Generates a signed authentication token for the given user.
     *
     * @param user the pure domain object representing the authenticated user.
     * @return a signed security token string (e.g., JWT).
     */
    String generateToken(User user);
}