package com.emobile.springtodo.exception;

public class InvalidToDoException extends RuntimeException {
    public InvalidToDoException(String message) {
        super(message);
    }
}