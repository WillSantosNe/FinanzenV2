package com.finanzen.api.application.service.auth;

import com.finanzen.api.application.dto.auth.LoginRequestDto;
import com.finanzen.api.application.exceptions.UserNotFoundException; // Agora usamos a exception correta!
import com.finanzen.api.application.ports.in.auth.AuthenticateUserPort;
import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

/**
 * Service implementation for the user authentication use case.
 * <p>
 * This class orchestrates the authentication flow by validating credentials
 * through an outbound provider and generating a token upon successful verification.
 * </p>
 */
@Service
public class AuthenticateUserUseCase implements AuthenticateUserPort {

    private final AuthenticationProviderPort authProvider;
    private final TokenGeneratorPort tokenGenerator;
    private final UserRepositoryPort userRepository;

    public AuthenticateUserUseCase(AuthenticationProviderPort authProvider,
                                   TokenGeneratorPort tokenGenerator,
                                   UserRepositoryPort userRepository) {
        this.authProvider = authProvider;
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    @Override
    public String authenticate(LoginRequestDto credentials) {
        // 1. Delega a verificação de credenciais para a porta de infraestrutura
        authProvider.authenticate(credentials.email(), credentials.password());

        // 2. Busca o domínio puro e lança nossa exception de domínio se falhar
        User user = userRepository.findByEmail(credentials.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + credentials.email()));

        // 3. Gera o token usando a porta de saída de tokens
        return tokenGenerator.generateToken(user);
    }
}