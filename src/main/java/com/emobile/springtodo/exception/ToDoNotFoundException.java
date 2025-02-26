package com.emobile.springtodo.exception;

/**
 * Исключение, которое выбрасывается, если задача ToDo не найдена по ID.
 *
 * @author PavelOkhrimchuk
 */
public class ToDoNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения с ID задачи.
     *
     * @param id Идентификатор задачи.
     */
    public ToDoNotFoundException(Long id) {
        super("ToDo item with id " + id + " not found");
    }
}
