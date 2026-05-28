package com.finanzen.api.application.ports.in.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.user.User;

/**
 * Inbound Port (Driving Port) for retrieving a paginated list of users.
 * <p>
 * This interface defines the contract for listing system users. By utilizing
 * the generic {@link PageResult} (a framework-agnostic wrapper), it prevents
 * infrastructure-specific pagination classes from leaking into the domain layer.
 * </p>
 */
public interface FindAllUsersPort {

    /**
     * Executes the use case to retrieve a paginated list of all system users.
     *
     * @param page the zero-based index of the page to retrieve.
     * @param size the maximum number of items per page.
     * @return a pure {@link PageResult} containing the requested {@link User} objects.
     */
    PageResult<User> findAll(int page, int size);
}