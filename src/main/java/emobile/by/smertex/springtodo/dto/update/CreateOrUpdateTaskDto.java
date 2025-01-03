package emobile.by.smertex.springtodo.dto.update;


import emobile.by.smertex.springtodo.database.entity.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Сущность для создания или обновления задачи в базе данных")
@Builder
public record CreateOrUpdateTaskDto(@NotBlank(message = "Имя задачи не может быть пустой") String name,
                                    @NotNull(message = "Статус не может быть пустым") Status status,
                                    @NotNull(message = "Приоритет не может быть пустым") Priority priority,
                                    @NotBlank(message = "Описание не может быть пустым") String description,
                                    @NotBlank(message = "Почта испольнителя не может быть пустой") @Email(message = "Почта некорректна") String performerEmail) {
}
