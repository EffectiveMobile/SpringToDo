package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.database.entity.realisation.Task;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с задачами
 */
public interface TaskService {
    Optional<Task> findById(UUID id);

    /**
     * Передает в репозиторий фильтр и условия пагинации. В случае, если
     * пользователь попытается получить задачи, где он не является исполнителем, будет выведен пустой список.
     * ADMIN игнорирует данное ограничение
     */
    List<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable);

    /**
     * Метод для сохранения задачи на основе DTO. При возникновении ошибки, указанной явно, будет откат транзакции
     */
    Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto);

    /**
     * Метод для обновлении задачи на основе DTO. Обновление способен соврешить либо исполнитель задачи, либо ADMIN
     */
    Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto);

    /**
     * Метод для удаления задачи, который доступен только пользователям с ролью ADMIN
     */
    boolean delete(UUID id);
}
