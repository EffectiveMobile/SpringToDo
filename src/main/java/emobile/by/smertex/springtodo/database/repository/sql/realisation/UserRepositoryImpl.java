package emobile.by.smertex.springtodo.database.repository.sql.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.database.repository.sql.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String FIND_BY_EMAIL_SQL = """
                                                    SELECT id, email, password, role
                                                    FROM users
                                                    WHERE email = ?
                                                    """;

    private static final String FIND_BY_ID_SQL = """
                                                 SELECT id, email, password, role
                                                 FROM users
                                                 WHERE id = ?
                                                 """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_EMAIL_SQL,
                    (resultSet, rowNum) -> buildEntity(resultSet),
                    email
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_ID_SQL,
                    (resultSet, rowNum) -> buildEntity(resultSet),
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private User buildEntity(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getObject("id", UUID.class));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
    }
}
