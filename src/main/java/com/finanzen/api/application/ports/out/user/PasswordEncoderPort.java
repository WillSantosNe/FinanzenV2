package com.finanzen.api.application.ports.out.user;

/**
 * Outbound Port (Driven Port) for password encoding.
 * <p>
 * This interface abstracts the password hashing strategy. By using this port
 * instead of the concrete {@code PasswordEncoder} from Spring Security, we
 * ensure that the application layer remains completely decoupled from specific
 * cryptographic implementations.
 * </p>
 */
public interface PasswordEncoderPort {

    /**
     * Hashes a raw password into a secure format.
     *
     * @param rawPassword the plain-text password to be hashed.
     * @return the hashed password string.
     */
    String encode(String rawPassword);

    /**
     * Verifies if a raw password matches the previously hashed password.
     *
     * @param rawPassword     the plain-text password provided by the user.
     * @param encodedPassword the existing hashed password stored in the database.
     * @return true if they match, false otherwise.
     */
    boolean matches(String rawPassword, String encodedPassword);
}