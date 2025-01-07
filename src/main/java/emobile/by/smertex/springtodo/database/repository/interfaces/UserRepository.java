package emobile.by.smertex.springtodo.database.repository.interfaces;

import emobile.by.smertex.springtodo.database.entity.realisation.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}
