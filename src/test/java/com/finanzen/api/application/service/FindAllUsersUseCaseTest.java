package com.finanzen.api.application.service;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.application.service.user.FindAllUsersUseCase;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class FindAllUsersUseCaseTest {

    @InjectMocks
    private FindAllUsersUseCase findAllUsersUseCase;

    @Mock private UserRepositoryPort repository;

    @Test
    public void should_return_list_of_users_when_users_exist(){
        // Arrange
        User user1 = new User(1L, "email1@gmail.com", "password", Role.USER);
        User user2 = new User(2L, "email2@gmail.com", "password", Role.USER);
        List<User> users = List.of(user1, user2);

        PageResult<User> page = new PageResult<>(users, 0, 2, 1);

        BDDMockito.given(repository.findAllSystemWide(anyInt(), anyInt())).willReturn(page);

        // Act
        PageResult<User> pageResult = findAllUsersUseCase.findAll(0,10);

        // Assert
        assertEquals(2, pageResult.data().size());
        assertEquals(2, pageResult.totalItems());
        assertEquals(1L, pageResult.data().get(0).getId());
    }

    @Test
    public void should_return_empty_list_when_database_is_empty(){
        // Arrange
        List<User> emptyList = new ArrayList<>();
        PageResult<User> page = new PageResult<>(emptyList, 0, 0, 1);

        BDDMockito.given(repository.findAllSystemWide(anyInt(), anyInt())).willReturn(page);

        // Act
        PageResult<User> pageResult = findAllUsersUseCase.findAll(0,10);

        // Assert
        assertEquals(0, pageResult.data().size());
        assertEquals(0, pageResult.totalItems());
        assertEquals(Collections.emptyList(),  pageResult.data());
    }
}
