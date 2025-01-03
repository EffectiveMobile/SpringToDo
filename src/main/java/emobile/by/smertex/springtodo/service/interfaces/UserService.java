package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.database.entity.realisation.User;

import java.util.Optional;

/**
 * Сервис для работы с пользователем
 */
public interface UserService {
    Optional<User> findByEmail(String email);
}
