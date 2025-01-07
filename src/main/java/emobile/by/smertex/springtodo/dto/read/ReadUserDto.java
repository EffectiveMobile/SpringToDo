package emobile.by.smertex.springtodo.dto.read;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Сущность пользователя")
@Builder
public record ReadUserDto(UUID id,
                          String email,
                          Role role) {
}
