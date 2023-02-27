package com.codefolio.exception.exceptions;

/**
 * Exception class for situations where credentials are already in use.
 */
public class CredentialsAlreadyInUseException extends RuntimeException {

    public CredentialsAlreadyInUseException(String message) {
        super(message);
    }
}
