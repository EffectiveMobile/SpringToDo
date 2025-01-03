package emobile.by.smertex.springtodo.dto.filter;

import by.smertex.database.entity.realisation.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
@Schema(description = "Фильтр по пользователю")
public record UserFilter(@Email(message = "Почта некорректна") String email,
                         Role role) {
}
