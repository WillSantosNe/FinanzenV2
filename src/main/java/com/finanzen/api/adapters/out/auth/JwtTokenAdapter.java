package com.finanzen.api.adapters.out.auth;

import com.finanzen.api.adapters.out.user.JpaUserEntity;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.domain.user.User;
import com.finanzen.api.infrastructure.config.TokenService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final TokenService tokenService;

    public JwtTokenAdapter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public String generateToken(User user) {
        JpaUserEntity userDetails = new JpaUserEntity();
        userDetails.setId(user.getId());
        userDetails.setEmail(user.getEmail());
        userDetails.setRole(user.getRole());

        return tokenService.generateToken(userDetails);
    }
}
