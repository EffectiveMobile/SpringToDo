package emobile.by.smertex.springtodo.controller.interfaces;

import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Контроллер аутентификации"
)
public interface AuthController {

    @Operation(
            summary = "Возвращает токен",
            description = "Возвращает JWT-токен, если в базе данных присутсвует пользователь, который проходит аутентификацию"
    )
    ResponseEntity<?> createAuthToken(JwtRequest authRequest);
}
