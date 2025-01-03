package emobile.by.smertex.springtodo.dto.security;

/**
 * DTO, которое применяется на уровне сервисов для взаимодействием с пользователем, который прошел процесс авторизации,
 * то есть прошел через JwtRequestFilter
 */
public record SecurityUserDto(String email,
                              boolean isAdmin) {
}
