package com.finanzen.api.application.exceptions;

/**
 * Application-level exception thrown when a requested transaction cannot be found.
 */
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Constructs a new TransactionNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining which transaction was not found.
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}