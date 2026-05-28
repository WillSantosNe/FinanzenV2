package com.finanzen.api.application.exceptions;

/**
 * Application-level exception thrown when a requested user cannot be found.
 * <p>
 * By using this custom domain-centric exception, we ensure that the core
 * application layer does not leak infrastructure details (such as database
 * or framework-specific errors) to the outer web adapters.
 * </p>
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the error context.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}