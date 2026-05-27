package com.finanzen.api.application.service.user;

import com.finanzen.api.application.exceptions.DuplicateEmailException;
import com.finanzen.api.application.ports.in.user.CreateUserPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase implements CreateUserPort {

    private final UserRepositoryPort repository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user in the system.
     *
     * @param user the user object containing the  details.
     * @return the created {@link User} domain object.
     */
    @Override
    public User create(User user) {

        // Verifica se email já existe
        if(repository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("The email address provided is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);

        return repository.save(user);
    }
}
