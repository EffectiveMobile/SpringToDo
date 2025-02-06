package com.emobile.springtodo.mappers;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entities.Task;

import java.util.List;

/**
 * <pre>
 *     Маппер для {@link }, {@link }
 * </pre>
 */
public interface TaskMapper {

    /**
     * Метод, конвертирующий {@link TaskDto} в {@link Task}
     *
     * @param tasksDto DTO объекта задачи
     * @return Сконвертированный объект задачи
     */
    Task convertDtoToTasks(TaskDto tasksDto);

    /**
     * Метод, конвертирующий {@link Task} в {@link TaskDto}
     *
     * @param tasks Сущность задачи из базы данных
     * @return Сконвертированный DTO объекта задачи
     */
    TaskDto convertTasksToDto(Task tasks);

    /**
     * Метод, сравнивающий поля Tasks из БД с TasksDto
     *
     * @param tasksDto DTO объекта задачи
     * @param tasks    Сущность задачи из базы данных
     * @return Обновленный объект задачи {@link }
     */
    void compareTaskAndDto(TaskDto tasksDto, Task tasks);

    List<TaskDto> transferListTasksToDto(List<Task> tasksList);

}
