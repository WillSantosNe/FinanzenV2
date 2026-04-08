package com.finanzen.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encoding.
 * <p>
 * This configuration is isolated from the main SecurityConfig to prevent 
 * circular dependency issues during the Spring Context initialization.
 * It provides the BCrypt hashing algorithm as the standard password encoder.
 * </p>
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Creates and exposes the BCrypt password encoder as a Spring Bean.
     *
     * @return the {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
