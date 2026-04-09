package com.finanzen.api.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finanzen.api.application.exception.DuplicateEmailException;
import com.finanzen.api.domain.Role;
import com.finanzen.api.domain.User;
import com.finanzen.api.infrastructure.dto.UserCreateDto;
import com.finanzen.api.infrastructure.repository.UserMapper;
import com.finanzen.api.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Application service responsible for authentication and user management workflows.
 * <p>
 * This service acts as the orchestrator between the inbound web requests and the 
 * outbound database operations, ensuring that business rules (like password hashing) 
 * are applied to the pure Domain model before persistence.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    // @RequiredArgsConstructor só considera se tiver a palavra final declarada
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user in the system.
     * <p>
     * Encodes the raw password using BCrypt, creates a pure Domain User with a 
     * default role of USER, and maps it to an entity for database persistence.
     * </p>
     *
     * @param dto the data transfer object containing the user's email and raw password.
     */
    public void register(UserCreateDto dto) {
        // Verificando se já existe email
        //  Barramos aqui já para não passar a responsabilidade para o repository.
        if(repository.existsByEmail(dto.email())){
            throw new DuplicateEmailException("The email address provided is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = new User(null, dto.email(), encodedPassword, Role.USER);

        // Uso de null no ID pois é autoincrementado
        repository.save(UserMapper.toEntity(user));
    }
}
