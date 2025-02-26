package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoItemDto;

import java.util.List;

/**
 * Сервис для управления задачами ToDo.
 * Содержит методы для получения, создания, обновления и удаления задач.
 */
public interface ToDoService {

    /**
     * Получить все задачи с поддержкой пагинации.
     *
     * @param limit  Количество задач для выборки.
     * @param offset Смещение для выборки.
     * @return Список задач в виде DTO.
     */
    List<ToDoItemDto> getAll(int limit, int offset);

    /**
     * Получить задачу по ID.
     * @param id Идентификатор задачи.
     * @return DTO задачи.
     */
    ToDoItemDto getById(Long id);

    /**
     * Создать новую задачу.
     * @param dto DTO задачи для создания.
     * @return Созданная задача в виде DTO.
     */
    ToDoItemDto create(ToDoItemDto dto);

    /**
     * Обновить задачу по ID.
     * @param id Идентификатор задачи для обновления.
     * @param dto DTO с новыми данными задачи.
     * @return Обновленная задача в виде DTO.
     */
    ToDoItemDto update(Long id, ToDoItemDto dto);

    /**
     * Удалить задачу по ID.
     * @param id Идентификатор задачи для удаления.
     */
    void delete(Long id);
}
