package com.finanzen.api.application.service;

import com.finanzen.api.application.exceptions.UserNotFoundException;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.application.service.user.FindUserByIdUseCase;
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

/**
 * Unit tests for {@link FindUserByIdUseCase}.
 * <p>
 * Ensures that the core business rules for find user by id are strictly applied,
 * isolating external dependencies such as databases and message brokers via Mockito.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
public class FindUserByIdUseCaseTest {

    @InjectMocks
    public FindUserByIdUseCase findUserByIdUseCase;

    @Mock private UserRepositoryPort repository;

    /**
     * Tests the successful find a user by id.
     * <p>
     * <b>Scenario:</b> A valid and existing id is provided<br>
     * <b>Action:</b> The use case is executed to find user by id.<br>
     * <b>Expected Result:</b> The user is successfully found and returned.
     * </p>
     */
    @Test
    public void should_return_user_when_id_is_valid(){
        // Arrange
        Long rawId = 1L;
        User user = new User(1L, "email@gmail.com", "password", Role.USER);

        BDDMockito.given(repository.findById(rawId)).willReturn(Optional.of(user));

        // Act
        User userResult = findUserByIdUseCase.findById(rawId);

        // Assert
        assertEquals(1L, userResult.getId());
        assertEquals("email@gmail.com", userResult.getEmail());
        assertEquals("password", userResult.getPassword());
        assertEquals(Role.USER, userResult.getRole());
    }


    /**
     * Tests the rejection of user search when a non-existing id is provided.
     * <p>
     * <b>Scenario:</b> A non-existing id is provided.<br>
     * <b>Action:</b> The use case is executed to find a user by id.<br>
     * <b>Expected Result:</b> The user is not found, the flow is aborted, and a UserNotFoundException is thrown.
     * </p>
     */
    @Test
    public void should_throw_user_not_found_exception_when_id_does_not_exist(){
        // Arrange
        Long rawId = 1L;
        BDDMockito.given(repository.findById(rawId)).willReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFoundException.class, () -> findUserByIdUseCase.findById(rawId));
    }
}
