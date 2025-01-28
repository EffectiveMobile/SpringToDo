package emobile.by.smertex.springtodo.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Данные аутентификации")
@Builder
public record JwtRequest(@Email String username,
                         @NotBlank String password) {
}
