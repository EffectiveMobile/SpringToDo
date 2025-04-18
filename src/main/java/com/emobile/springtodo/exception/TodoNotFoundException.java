package com.emobile.springtodo.exception;

/**
 * Исключение, выбрасываемое, если задача с указанным ID не найдена.
 *
 * @author Мельников Никита
 */
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo with id " + id + " not found");
    }
}
