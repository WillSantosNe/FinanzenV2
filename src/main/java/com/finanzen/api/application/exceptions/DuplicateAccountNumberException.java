package com.finanzen.api.application.exceptions;


public class DuplicateAccountNumberException extends RuntimeException {
    public DuplicateAccountNumberException(String message){
        super(message);
    }
}
