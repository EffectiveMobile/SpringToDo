package emobile.by.smertex.springtodo.database.repository.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.User;
import emobile.by.smertex.springtodo.database.repository.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
