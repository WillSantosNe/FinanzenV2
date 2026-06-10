package com.finanzen.api.application.ports.out.user;

import com.finanzen.api.domain.user.User;

public interface UserEventPublisherPort {

    /**
     * Publishes an event indicating that a new user has been successfully created.
     *
     * @param user the domain entity containing the created user details.
     */
    void publishUserCreated(User user);
}
