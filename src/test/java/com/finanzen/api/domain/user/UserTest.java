package com.finanzen.api.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: User Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create a valid user with all properties correctly assigned")
    void shouldCreateValidUser() {
        // Arrange
        Long id = 1L;
        String email = "willi@gmail.com";
        String password = "hashed_secure_password_123";
        Role role = Role.USER;

        // Act
        User user = new User(id, email, password, role);

        // Assert
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    @DisplayName("Should allow mutation of mutable fields via setters")
    void shouldAllowFieldMutations() {
        // Arrange
        User user = new User(1L, "old@gmail.com", "old_pass", Role.USER);

        // Act
        user.setEmail("new@gmail.com");
        user.setPassword("new_pass");
        user.setRole(Role.ADMIN);

        // Assert
        assertEquals("new@gmail.com", user.getEmail());
        assertEquals("new_pass", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }
}