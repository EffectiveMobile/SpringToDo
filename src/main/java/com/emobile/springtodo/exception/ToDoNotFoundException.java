package com.emobile.springtodo.exception;

public class ToDoNotFoundException extends RuntimeException {
    public ToDoNotFoundException(Long id) {
        super("ToDo item with id " + id + " not found");
    }
}
