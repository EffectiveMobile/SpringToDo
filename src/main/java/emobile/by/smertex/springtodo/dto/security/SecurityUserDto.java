package emobile.by.smertex.springtodo.dto.security;

/**
 * DTO, которое применяется на уровне сервисов для взаимодействия с пользователем, прошеднего процесс авторизации,
 * то есть прошел через JwtRequestFilter
 */
public record SecurityUserDto(String email,
                              boolean isAdmin) {
}
