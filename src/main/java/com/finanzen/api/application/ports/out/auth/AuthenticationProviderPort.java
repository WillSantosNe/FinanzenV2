package com.finanzen.api.application.ports.out.auth;

/**
 * Outbound Port (Driven Port) for credential verification.
 * <p>
 * This interface defines the contract for validating user credentials.
 * By abstracting this operation, the application core remains independent
 * of any specific security framework or authentication provider (e.g., Spring Security,
 * LDAP, or OAuth2).
 * </p>
 */
public interface AuthenticationProviderPort {

    /**
     * Authenticates the user based on the provided credentials.
     *
     * @param email    the email address (or username) to verify.
     * @param password the raw password provided by the user.
     * @throws RuntimeException (or a specific authentication exception) if the
     * credentials provided are invalid.
     */
    void authenticate(String email, String password);
}