package com.emobile.springtodo.exceptions;

public class ErrorInputDataException extends IllegalArgumentException {
    public ErrorInputDataException(String msg) {
        super(msg);
    }
}
