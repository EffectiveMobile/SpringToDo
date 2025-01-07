package emobile.by.smertex.springtodo.database.repository.sql.interfaces;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}
