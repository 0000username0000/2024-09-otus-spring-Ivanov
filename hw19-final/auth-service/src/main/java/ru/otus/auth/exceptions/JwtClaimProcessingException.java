package ru.otus.auth.exceptions;

public class JwtClaimProcessingException extends RuntimeException {

    public JwtClaimProcessingException(String message) {
        super(message);
    }

    public JwtClaimProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}