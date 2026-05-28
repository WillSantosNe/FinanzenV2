package com.finanzen.api.application.service.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.user.FindAllUsersPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for listing system users.
 * <p>
 * This service implements the {@link FindAllUsersPort}. It delegates the
 * pagination and retrieval logic to the repository port, ensuring a clean
 * boundary between the application and infrastructure layers.
 * </p>
 */
@Service
public class FindAllUsersUseCase implements FindAllUsersPort {

    private final UserRepositoryPort repository;

    public FindAllUsersUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to retrieve a paginated list of all users.
     *
     * @param page the zero-based index of the page.
     * @param size the maximum number of records per page.
     * @return a pure {@link PageResult} containing the retrieved users.
     */
    @Override
    public PageResult<User> findAll(int page, int size) {
        return repository.findAllSystemWide(page, size);
    }
}