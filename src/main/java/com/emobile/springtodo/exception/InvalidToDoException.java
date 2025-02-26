package com.emobile.springtodo.exception;

/**
 * Исключение, которое выбрасывается, если задача ToDo является недействительной.
 */
public class InvalidToDoException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public InvalidToDoException(String message) {
        super(message);
    }
}
