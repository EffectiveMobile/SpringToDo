package com.emobile.springtodo.core.exception.customexceptions;

public class CustomDataAccessException extends RuntimeException {
    public CustomDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomDataAccessException(String message) {
        super(message);
    }
}
