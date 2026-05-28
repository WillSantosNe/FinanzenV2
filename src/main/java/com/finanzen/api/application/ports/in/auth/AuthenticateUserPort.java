package com.finanzen.api.application.ports.in.auth;

import com.finanzen.api.application.dto.auth.LoginRequestDto;

/**
 * Inbound Port (Driving Port) for user authentication.
 * <p>
 * This interface defines the use case contract for authenticating a user
 * in the system. It serves as the entry boundary for external adapters
 * (such as the AuthController) to trigger the core security logic,
 * completely decoupling the web layer from the actual authentication implementation.
 * </p>
 */
public interface AuthenticateUserPort {

    /**
     * Executes the authentication use case.
     *
     * @param credentials the validated data transfer object containing the user's email and password.
     * @return a generated JWT (JSON Web Token) string upon successful authentication.
     * @throws RuntimeException (or a specific security exception) if the credentials are invalid or the user is not found.
     */
    String authenticate(LoginRequestDto credentials);
}