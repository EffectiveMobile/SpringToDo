package emobile.by.smertex.springtodo.dto.filter;

import emobile.by.smertex.springtodo.database.entity.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "Фильтр задач")
@Builder
public record TaskFilter(@Valid @NotNull(message = "Данное поле не может быть равно null") UserFilter createdBy,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         @Valid @NotNull(message = "Данное поле не может быть равно null") UserFilter performer,
                         Status status,
                         Priority priority,
                         String name) {
}
