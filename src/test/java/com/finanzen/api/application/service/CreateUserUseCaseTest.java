package com.finanzen.api.application.service;

import com.finanzen.api.application.exceptions.DuplicateEmailException;
import com.finanzen.api.application.ports.out.user.PasswordEncoderPort;
import com.finanzen.api.application.ports.out.user.UserEventPublisherPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.application.service.user.CreateUserUseCase;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @InjectMocks
    public CreateUserUseCase createUserUseCase;

    @Mock private UserRepositoryPort repository;
    @Mock private PasswordEncoderPort passwordEncoder;
    @Mock private UserEventPublisherPort userEventPublisherPort;

    @Test
    public void should_create_user_encrypt_password_and_publish_event(){

        // Arrange
        User rawUser = new User(null, "teste@email.com", "senha123", null);
        User savedUser = new User(1L, "teste@email.com", "HASH", Role.USER);

        BDDMockito.given(repository.existsByEmail(rawUser.getEmail())).willReturn(false);
        BDDMockito.given(passwordEncoder.encode(rawUser.getPassword())).willReturn("HASH");
        BDDMockito.given(repository.save(any(User.class))).willReturn(savedUser);

        // Act
        User userResult = createUserUseCase.create(rawUser);

        // Assert
        assertEquals(1L, userResult.getId());
        assertEquals("HASH", userResult.getPassword());

        BDDMockito.then(userEventPublisherPort).should().publishUserCreated(any(User.class));
    }


    @Test
    public void should_throw_duplicate_email_exception_when_email_already_exists(){
       // Arrange
        User rawUser = new User(null, "duplicado@email.com", "senha123", null);
        BDDMockito.given(repository.existsByEmail(rawUser.getEmail())).willReturn(true);

       // Act + Assert
        assertThrows(DuplicateEmailException.class, () -> createUserUseCase.create(rawUser));

        // Nunca pode chamar o repository
        BDDMockito.then(repository).should(never()).save(any());
        BDDMockito.then(userEventPublisherPort).should(never()).publishUserCreated(any());
    }

}
