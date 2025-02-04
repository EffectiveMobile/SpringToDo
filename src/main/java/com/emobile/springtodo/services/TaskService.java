package com.emobile.springtodo.services;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.exceptions.DataCalendarNotBeNullException;
import com.emobile.springtodo.exceptions.EntityNotFoundException;
import com.emobile.springtodo.exceptions.StatusTaskNotBeNullException;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     Интерфейс для работы с сущностью Tasks
 * </pre>
 */
public interface TaskService {

    /**
     * Метод для создания задачи
     *
     * @param tasksDto DTO объекта задачи, содержащий данные для создания
     * @return Созданный объект-задача в виде {@link TaskDto}
     */
    TaskDto createTasks(TaskDto tasksDto) throws StatusTaskNotBeNullException, DataCalendarNotBeNullException;

    /**
     * Метод для редактирования задачи
     *
     * @param tasksDto DTO объекта задачи с обновленными данными
     * @return {@link Optional} с изменённым объектом-задачей, если задача найдена и успешно обновлена
     */
    Optional<TaskDto> changeTasks(TaskDto tasksDto) throws EntityNotFoundException;

    /**
     * Метод для получения задач по заголовку
     *
     * @param authorOrExecutor Название задачи
     * @param offset           Номер страницы для пагинации
     * @param limit            Лимит записей на странице
     * @return {@link Optional} со списком задач в виде {@link List<>}
     */
    Optional<List<TaskDto>> getTasksByTitle(String authorOrExecutor, Integer offset, Integer limit);

    /**
     * Метод для удаления задачи
     *
     * @param idTasks ID задачи, которую нужно удалить
     * @return true - если задача найдена и удалена успешно
     */
    boolean deleteTasks(Long idTasks);

}
