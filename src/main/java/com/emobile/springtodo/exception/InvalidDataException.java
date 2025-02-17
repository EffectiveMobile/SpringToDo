package com.emobile.springtodo.exception;

/**
 * Исключение, выбрасываемое при передаче недопустимых данных.
 * Используется для обработки ошибок валидации входных данных.
 *
 * @author Мельников Никита
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
