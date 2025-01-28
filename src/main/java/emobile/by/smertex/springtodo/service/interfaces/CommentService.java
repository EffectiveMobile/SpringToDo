package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с комментариями
 */
public interface CommentService {

    /**
     * Передает в репозиторий фильтр, условия пагинации. В случае, если
     * пользователь попытается получить комментарии из задачи, где он не является исполнителем, будет выведен пустой список.
     * ADMIN игнорирует данное ограничение
     */
    List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable);

    /**
     * Сохранение комментария под существующей задачей. Пользователь может добавить комментарий только в том случае, если задача существует и он
     * является исполнителем. Условие с исполнителем ADMIN может игнорировать
     */
    ReadCommentDto add(UUID taskId, CreateOrUpdateCommentDto dto);

    /**
     * Обновление комментария. Данная опция доступна только создателю комментария. ADMIN, включительно, не может проигнорировать данное ограничение
     */
    ReadCommentDto update(UUID commentId, CreateOrUpdateCommentDto dto);
}
