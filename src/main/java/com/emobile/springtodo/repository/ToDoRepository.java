package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoItem;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с задачами ToDo.
 * Содержит методы для поиска, сохранения, обновления и удаления задач.
 *
 * @author PavelOkhrimchuk
 */
public interface ToDoRepository {

    /**
     * Получить все задачи с поддержкой пагинации.
     *
     * @param limit  Количество задач для выборки.
     * @param offset Смещение для выборки.
     * @return Список задач.
     */
    List<ToDoItem> findAll(int limit, int offset);

    /**
     * Найти задачу по ID.
     * @param id Идентификатор задачи.
     * @return Опциональная задача.
     */
    Optional<ToDoItem> findById(Long id);

    /**
     * Сохранить новую задачу.
     * @param toDoItem Задача, которую нужно сохранить.
     */
    void save(ToDoItem toDoItem);

    /**
     * Обновить задачу.
     * @param toDoItem Задача с обновленными данными.
     */
    void update(ToDoItem toDoItem);

    /**
     * Удалить задачу по ID.
     * @param id Идентификатор задачи.
     */
    void deleteById(Long id);
}
