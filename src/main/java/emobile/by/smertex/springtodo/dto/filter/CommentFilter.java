package emobile.by.smertex.springtodo.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Фильтр комментариев")
@Builder
public record CommentFilter(@Valid @NotNull(message = "Данное поле не может быть равно null") UserFilter createdBy) {
}
