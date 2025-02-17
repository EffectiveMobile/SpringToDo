package com.emobile.springtodo.repository.contract;

import com.emobile.springtodo.model.Todo;

import java.util.List;

/**
 * Интерфейс TodoRepository предоставляет методы для работы с задачами в базе данных.
 *
 * @author Мельников Никита
 */
public interface TodoRepository {

    /**
     * Возвращает список задач с пагинацией.
     *
     * @param limit  максимальное количество задач в результате
     * @param offset смещение для пагинации
     * @return список задач
     */
    List<Todo> findAll(int limit, int offset);

    /**
     * Находит задачу по её уникальному идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача с указанным ID или null, если задача не найдена
     */
    Todo findById(Long id);

    /**
     * Сохраняет новую задачу в базе данных.
     *
     * @param todo задача для сохранения
     * @return сохранённая задача с присвоенным ID
     */
    Todo save(Todo todo);

    /**
     * Обновляет существующую задачу в базе данных.
     *
     * @param todo задача с обновлёнными данными
     * @return обновлённая задача
     */
    Todo update(Todo todo);

    /**
     * Удаляет задачу из базы данных по её уникальному идентификатору.
     *
     * @param id идентификатор задачи для удаления
     */
    void delete(Long id);
}
