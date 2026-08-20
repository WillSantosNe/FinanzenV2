package com.finanzen.api.application.service.auth;

import com.finanzen.api.application.dto.auth.LoginRequestDto;
import com.finanzen.api.application.exceptions.UserNotFoundException;
import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import com.finanzen.api.application.ports.out.auth.TokenGeneratorPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUserUseCaseTest {

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock private AuthenticationProviderPort authProvider;
    @Mock private TokenGeneratorPort tokenGenerator;
    @Mock private UserRepositoryPort userRepository;

    @Test
    public void should_authenticate_user_and_return_token_successfully(){
        // Arrange
        String email = "email@gmail.com";
        String password = "password";
        String token = "token";

        LoginRequestDto credentials = new LoginRequestDto(email, password);
        User userFound = new User(1L, email, password, Role.USER);

        BDDMockito.given(userRepository.findByEmail(credentials.email()))
                .willReturn(Optional.of(userFound));

        BDDMockito.given(tokenGenerator.generateToken(userFound)).willReturn(token);

        // Act
        String tokenGenerated = authenticateUserUseCase.authenticate(credentials);

        // Assert
        assertEquals(token, tokenGenerated);
        BDDMockito.then(authProvider).should().authenticate(credentials.email(), credentials.password());
    }

    @Test
    void should_throw_exception_when_authentication_fails() {
        // Arrange
        LoginRequestDto credentials = new LoginRequestDto("test@email.com", "password123");

        BDDMockito.willThrow(RuntimeException.class)
                .given(authProvider).authenticate(credentials.email(), credentials.password());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticateUserUseCase.authenticate(credentials));

        BDDMockito.then(authProvider).should().authenticate(any(), any());
        BDDMockito.then(userRepository).should(never()).findByEmail(any());
        BDDMockito.then(tokenGenerator).should(never()).generateToken(any());
    }

    @Test
    void should_throw_user_not_found_exception_when_email_is_invalid(){
        // Arrange
        LoginRequestDto credentials = new LoginRequestDto("test@email.com", "password123");

        BDDMockito.given(userRepository.findByEmail(credentials.email())).willThrow(
                new UserNotFoundException("User not found with email: " + credentials.email()));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authenticateUserUseCase.authenticate(credentials));
        BDDMockito.then(authProvider).should(times(1)).authenticate(any(), any());
        BDDMockito.then(tokenGenerator).should(never()).generateToken(any());
    }
}
