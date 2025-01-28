package emobile.by.smertex.springtodo.controller.interfaces;

import emobile.by.smertex.springtodo.dto.exception.ApplicationResponse;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.dto.request.CommentRequest;
import emobile.by.smertex.springtodo.dto.request.TaskRequest;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Контроллер задач",
        description = "Позволяет взаимодействовать с задачами и их комментариями"
)
public interface TaskController {
    @Operation(
            summary = "Возвращает список всех задач",
            description = """
                          Поиск происходит по средством фильтрации и пагинации. Если пользователь попытается получить задачи, где он не является исполнителем,
                          то будет получен пустой список. ADMIN игнорирует данное ограничение и может получать любые задачи.
                          """
    )
    @GetMapping
    List<ReadTaskDto> findAll(@Parameter(name = "Запрос с фильтрацией и пагинацией задач") TaskRequest request, Pageable pageable);

    @Operation(
            summary = "Создает задачу",
            description = """
                          При вводе неккоректных или пустых данных будет выдана ошибка. Как обычные пользователи, так и администраторы, могут создавать
                          для других пользователей задачи. Создать задачу и делегировать ее на несуществующего пользователя невозможно
                          """
    )
    ReadTaskDto create(@Parameter(name = "Тело сохранения") CreateOrUpdateTaskDto dto);

    @Operation(
            summary = "Обновляет задачу",
            description = """
                          При вводе неккоректных или пустых данных будет выдана ошибка. Как обычные пользователи, так и ADMIN, могут делегировать
                          права на задачу другому пользователю. В случае с обычным пользователем, он теряет возможность взаимодействовать с задачей в тот момент, 
                          как перестает быть ее исполнителем. Делегировать задачу на несуществующего пользователя невозможно
                          """
    )
    ReadTaskDto updateTask(@Parameter(description = "Идентификатор задачи") UUID id,
                           @Parameter(name = "Тело обновления") CreateOrUpdateTaskDto dto);

    @Operation(
            summary = "Удаляет задачу",
            description = """
                          Метод, доступный только тем, кто авторизован как ADMIN. Позволяет удалять задачи, как следствие комментарии к ним тоже удаляются.
                          При успешном удалении возвращается ответ "Task deleted" с кодом "200". При ошибке "The task was not deleted" с кодом "400"
                          """
    )
    ResponseEntity<ApplicationResponse> deleteTask(@Parameter(description = "Идентификатор задачи") UUID id);

    @Operation(
            summary = "Поиск комментариев по задаче",
            description = """
                          Поиск комментариев по id задачи, а также фильтру и пагинации. Если пользователь попытается получить комментарии, где он не является исполнителем,
                          будет возвращен пустой список. ADMIN игнорирует данное ограничение
                          """
    )
    List<ReadCommentDto> findAllComment(@Parameter(description = "Идентификатор задачи") UUID id,
                                        @Parameter(name = "Запрос с фильтрацией и пагинацией комментариев") CommentRequest commentRequest,
                                        Pageable pageable);

    @Operation(
            summary = "Добавление комментария к задаче",
            description = """
                          Обычные пользователи могут добавлять комментарии только к тем задачам, в которых являются исполнителями. ADMIN игнорирует данное ограничение
                          """
    )
    ReadCommentDto addComment(@Parameter(description = "Идентификатор задачи") UUID id,
                              @Parameter(name = "Тело сохранения") CreateOrUpdateCommentDto dto);

    @Operation(
            summary = "Обновление комментария",
            description = """
                          Обновление содержание комментария, доступно только тому пользователю, что сам оставил комментарий. Данное ограничение накладывается и на ADMIN
                          """
    )
    ReadCommentDto updateComment(@Parameter(description = "Идентификатор комментария") UUID id,
                                 @Parameter(name = "Тело обновления") CreateOrUpdateCommentDto dto);
}
