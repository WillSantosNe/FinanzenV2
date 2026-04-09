package com.finanzen.api.application.exception;

/**
 * Business exception thrown when an attempt is made to register a user 
 * with an email address that is already in use.
 * <p>
 * By throwing this custom exception at the application layer, we prevent 
 * database constraint violations (DataIntegrityViolationException) and 
 * avoid leaking infrastructure errors to the end user.
 * </p>
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message){
        super(message);
    }
}
