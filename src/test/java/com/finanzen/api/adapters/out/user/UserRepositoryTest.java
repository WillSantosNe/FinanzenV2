package com.finanzen.api.adapters.out.user;

import com.finanzen.api.AbstractDatabaseTest;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(UserRepository.class) // Forçando a injetar o @Component do UserRepository
public class UserRepositoryTest extends AbstractDatabaseTest {

    // Injetando repositório real vindo do docker
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_save_and_find_user_successfully(){
        // Arrange
        User userToSave = new User(null, "email@gmail.com", "password", Role.USER);

        // Act
        User userSaved = userRepository.save(userToSave);
        Optional<User> userFound = userRepository.findByEmail("email@gmail.com");

        // Assert
        assertTrue(userFound.isPresent());
        assertEquals(userSaved.getId(), userFound.get().getId());
        assertEquals(userSaved.getEmail(), userFound.get().getEmail());
        assertEquals(userSaved.getPassword(), userFound.get().getPassword());
        assertEquals(userSaved.getRole(), userFound.get().getRole());
    }

    @Test
    public void should_throw_exception_when_saving_duplicated_email(){
        // Arrange
        User userToSave1 = new User(null, "email@gmail.com", "password", Role.USER);
        User userToSave2 = new User(null, "email@gmail.com", "password", Role.USER);

        // Act & Assert
        User userSaved1 = userRepository.save(userToSave1);
        entityManager.flush(); // Forcando o banco de dados a dar o insert
        entityManager.clear(); // Limpando a memoria do hibernate

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(userToSave2);
            entityManager.flush(); // Forcando o banco de dados a dar o insert
            });
    }
}