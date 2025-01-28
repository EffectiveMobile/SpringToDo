package emobile.by.smertex.springtodo.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Сущность для создания или обновления комментария в базе данных")
@Builder
public record CreateOrUpdateCommentDto(@NotBlank(message = "Комментарий не может быть пустым") String content) {
}
