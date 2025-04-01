package ru.otus.hw.exceptions;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6129548716336748817L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
