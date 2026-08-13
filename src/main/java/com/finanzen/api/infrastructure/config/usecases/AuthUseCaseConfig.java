package com.finanzen.api.infrastructure.config.usecases;

import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.application.service.auth.AuthenticateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {
    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(AuthenticationProviderPort authProvider,
                                                           TokenGeneratorPort tokenGenerator,
                                                           UserRepositoryPort userRepository){
        return new AuthenticateUserUseCase(authProvider, tokenGenerator, userRepository);

    }
}
