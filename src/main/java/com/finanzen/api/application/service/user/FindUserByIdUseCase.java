package com.finanzen.api.application.service.user;

import com.finanzen.api.application.exceptions.UserNotFoundException;
import com.finanzen.api.application.ports.in.user.FindUserByIdPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class FindUserByIdUseCase implements FindUserByIdPort {

    private final UserRepositoryPort repository;

    public FindUserByIdUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a specific user by its unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return the {@link User} domain object.
     * @throws UserNotFoundException if no user is found.
     */
    @Override
    public User findById(Long id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " not found in the system")
        );
    }
}
