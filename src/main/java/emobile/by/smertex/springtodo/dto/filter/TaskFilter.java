package emobile.by.smertex.springtodo.dto.filter;

import by.smertex.database.entity.realisation.enums.Priority;
import by.smertex.database.entity.realisation.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "Фильтр задач")
@Builder
public record TaskFilter(@Valid @NotNull(message = "Данное поле не может быть равно null") UserFilter createdBy,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime closedAt,
                         @Valid @NotNull(message = "Данное поле не может быть равно null") UserFilter performer,
                         Status status,
                         Priority priority,
                         String name) {
}
