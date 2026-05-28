package com.finanzen.api.adapters.out.auth;

import com.finanzen.api.adapters.out.user.JpaUserEntity;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.domain.user.User;
import com.finanzen.api.infrastructure.config.TokenService;
import org.springframework.stereotype.Component;

/**
 * Concrete Outbound Adapter responsible for generating security tokens.
 * <p>
 * This class implements the {@link TokenGeneratorPort} driven port. It intercepts
 * the pure domain {@link User} object, maps its state to an infrastructure-compliant
 * entity ({@link JpaUserEntity}), and delegates the final cryptographic token generation
 * to the internal {@link TokenService}.
 * </p>
 */
@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final TokenService tokenService;

    public JwtTokenAdapter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Maps the core domain object to an infrastructure layer object and generates the JWT.
     *
     * @param user the pure domain user model containing identity details.
     * @return a fully signed JSON Web Token (JWT) string.
     */
    @Override
    public String generateToken(User user) {
        // Mapeamento necessário para alimentar o TokenService nativo da infraestrutura
        JpaUserEntity userDetails = new JpaUserEntity();
        userDetails.setId(user.getId());
        userDetails.setEmail(user.getEmail());
        userDetails.setRole(user.getRole());

        return tokenService.generateToken(userDetails);
    }
}