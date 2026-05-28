package com.finanzen.api.application.service.user;

import com.finanzen.api.application.exceptions.UserNotFoundException;
import com.finanzen.api.application.ports.in.user.FindUserByIdPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for retrieving a specific user by ID.
 * <p>
 * This service implements the {@link FindUserByIdPort}. It ensures that the
 * retrieval process handles missing users gracefully by mapping the result
 * to a domain-specific {@link UserNotFoundException}.
 * </p>
 */
@Service
public class FindUserByIdUseCase implements FindUserByIdPort {

    private final UserRepositoryPort repository;

    public FindUserByIdUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to find a user by their unique identifier.
     *
     * @param id the unique identifier of the user.
     * @return the {@link User} domain object.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public User findById(Long id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " not found in the system")
        );
    }
}