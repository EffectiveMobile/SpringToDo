package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;

import java.util.Optional;

/**
 * Сервис, обеспечивающий взаимодействие с контекстом Spring Security. А именно с аутентификацией пользователя в системе.
 * Данный сервис также обеспечивает возможность взять пользователя из контекста.
 */
public interface AuthService {

    /**
     * Возвращает JWT при успешном прохождении аутентификации
     */
    String authentication(JwtRequest authRequest);

    Optional<SecurityUserDto> takeUserFromContext();

}
