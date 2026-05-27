package com.finanzen.api.application.service.auth;

import com.finanzen.api.application.dto.auth.LoginRequestDto;
import com.finanzen.api.application.ports.in.auth.AuthenticateUserPort;
import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase implements AuthenticateUserPort {

    private final AuthenticationProviderPort authenticationProviderPort;
    private final TokenGeneratorPort tokenGeneratorPort;
    private final UserRepositoryPort userRepositoryPort;

    public AuthenticateUserUseCase(AuthenticationProviderPort authenticationProviderPort,
                                   TokenGeneratorPort tokenGeneratorPort,
                                   UserRepositoryPort userRepositoryPort) {

        this.authenticationProviderPort = authenticationProviderPort;
        this.tokenGeneratorPort = tokenGeneratorPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public String authenticate(LoginRequestDto credentials) {

        // Infraestrutura verifica as credenciais
        authenticationProviderPort.authenticate(credentials.email(), credentials.password());

        User user = userRepositoryPort.findByEmail(credentials.email()).orElseThrow(
                () -> new RuntimeException("User not found with email: " + credentials.email())
        );

        return tokenGeneratorPort.generateToken(user);
    }
}
