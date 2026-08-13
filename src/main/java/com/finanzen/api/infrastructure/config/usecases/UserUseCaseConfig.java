package com.finanzen.api.infrastructure.config.usecases;

import com.finanzen.api.application.ports.out.user.PasswordEncoderPort;
import com.finanzen.api.application.ports.out.user.UserEventPublisherPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.application.service.user.CreateUserUseCase;
import com.finanzen.api.application.service.user.FindAllUsersUseCase;
import com.finanzen.api.application.service.user.FindUserByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositoryPort userRepositoryPort,
                                               PasswordEncoderPort passwordEncoderPort,
                                               UserEventPublisherPort userEventPublisherPort) {
        return new CreateUserUseCase(userRepositoryPort, passwordEncoderPort, userEventPublisherPort);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase(UserRepositoryPort repository) {
        return new FindAllUsersUseCase(repository);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepositoryPort repository) {
        return new FindUserByIdUseCase(repository);
    }
}
