package com.emobile.springtodo.exception;

public class ToDoNotFoundException extends RuntimeException{
    public ToDoNotFoundException(String message){
        super(message);
    }
}
