package emobile.by.smertex.springtodo.dto.read;

import emobile.by.smertex.springtodo.database.entity.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Сущность задачи")
@Builder
public record ReadTaskDto(UUID id,
                          String name,
                          String description,
                          Status status,
                          Priority priority,
                          ReadUserDto performer) {
}
