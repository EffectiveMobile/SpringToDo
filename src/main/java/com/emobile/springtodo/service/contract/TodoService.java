package com.emobile.springtodo.service.contract;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.exception.InvalidDataException;
import com.emobile.springtodo.model.Todo;

import java.util.List;

/**
 * Интерфейс TodoService предоставляет методы для управления задачами.
 * Этот сервис является связующим звеном между контроллером и репозиторием.
 *
 * @author Мельников Никита
 */
public interface TodoService {

    /**
     * Возвращает список задач с пагинацией.
     *
     * @param limit  максимальное количество задач в результате
     * @param offset смещение для пагинации
     * @return список задач
     */
    List<Todo> getAllTodos(int limit, int offset);

    /**
     * Находит задачу по её уникальному идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача с указанным ID
     * @throws TodoNotFoundException если задача не найдена
     */
    Todo getTodoById(Long id);

    /**
     * Создает новую задачу.
     *
     * @param createTodo DTO с данными для создания задачи
     * @return созданная задача
     * @throws InvalidDataException если данные для создания задачи недопустимы
     */
    Todo saveTodo(CreateTodo createTodo);

    /**
     * Обновляет существующую задачу.
     *
     * @param id          идентификатор задачи
     * @param updatedTodo DTO с обновленными данными задачи
     * @return обновленная задача
     * @throws TodoNotFoundException если задача не найдена
     * @throws InvalidDataException если данные для обновления задачи недопустимы
     */
    Todo updateTodo(Long id, UpdateTodo updatedTodo);

    /**
     * Удаляет задачу по её уникальному идентификатору.
     *
     * @param id идентификатор задачи
     * @throws TodoNotFoundException если задача не найдена
     */
    void deleteTodo(Long id);
}
