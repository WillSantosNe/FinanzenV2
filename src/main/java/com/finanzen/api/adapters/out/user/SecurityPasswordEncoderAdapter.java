package com.finanzen.api.adapters.out.user;

import com.finanzen.api.application.ports.out.user.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Concrete Outbound Adapter responsible for password hashing using Spring Security.
 * <p>
 * This adapter implements the {@link PasswordEncoderPort}, isolating the core
 * application layer from the specific cryptographic library managed by the framework.
 * </p>
 */
@Component
public class SecurityPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder springPasswordEncoder;

    public SecurityPasswordEncoderAdapter(PasswordEncoder springPasswordEncoder) {
        this.springPasswordEncoder = springPasswordEncoder;
    }

    /**
     * Hashes a raw password by delegating to Spring Security's configured encoder mechanism.
     *
     * @param rawPassword the plain-text password.
     * @return the safely hashed password string.
     */
    @Override
    public String encode(String rawPassword) {
        return springPasswordEncoder.encode(rawPassword);
    }

    /**
     * Validates a raw password against an existing secure hash.
     *
     * @param rawPassword     the plain-text password attempt.
     * @param encodedPassword the valid hashed password from storage.
     * @return true if the passwords match, false otherwise.
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}