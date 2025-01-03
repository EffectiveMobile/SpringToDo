package emobile.by.smertex.springtodo.dto.read;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Сущность комментария")
@Builder
public record ReadCommentDto(UUID id,
                             String content,
                             ReadUserDto createdBy,
                             LocalDateTime createdAt) {
}
