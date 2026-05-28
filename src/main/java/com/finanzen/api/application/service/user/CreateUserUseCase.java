package com.finanzen.api.application.service.user;

import com.finanzen.api.application.exceptions.DuplicateEmailException;
import com.finanzen.api.application.ports.in.user.CreateUserPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for user registration.
 * <p>
 * This service implements the {@link CreateUserPort}. It orchestrates the
 * business rules for user creation, including email uniqueness validation
 * and password security (hashing).
 * </p>
 */
@Service
public class CreateUserUseCase implements CreateUserPort {

    private final UserRepositoryPort repository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executes the use case to register a new user.
     *
     * @param user the pure domain object containing raw user registration data.
     * @return the created {@link User} domain object, including the assigned ID and role.
     * @throws DuplicateEmailException if the email is already registered in the system.
     */
    @Override
    public User create(User user) {
        // Regra de Negócio: Email deve ser único
        if(repository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("The email address provided is already in use.");
        }

        // Regra de Negócio: Senha deve ser criptografada e role padrão definida
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);

        return repository.save(user);
    }
}