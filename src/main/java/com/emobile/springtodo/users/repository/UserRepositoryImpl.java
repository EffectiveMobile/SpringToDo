package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.dto.in.UpdateUserAccount;
import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.utils.exception.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String MESSAGE = "User with email: {} was sent";

    private static final String MESSAGE_ID = "User with userid: {} was sent";

    @Override
    public List<User> getAllUsersList(Integer from, Integer size) {

        final String sqlQuery = """
                SELECT *
                FROM public.users
                OFFSET ?
                LIMIT ?
                """;

        return jdbcTemplate.query(sqlQuery, this::makeUser, from, size);
    }

    @Override
    public User getUserById(Long userId) {

        final String sqlQuery = """
                SELECT *
                FROM public.users
                WHERE users.id = ?
                """;

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, userId);

        if (!userRows.next()) {
            log.warn("User with userId: {} are not present in Db!", userId);
            throw new ObjectNotFoundException("User not present in Db!");
        }

        final String checkQuery = """
                SELECT *
                FROM public.users
                WHERE users.id = ?
                """;

        log.info(MESSAGE, userId);
        return jdbcTemplate.queryForObject(checkQuery, this::makeUser, userId);
    }

    @Override
    public User createUserAccount(NewUserRequestDto newUserDto) {

        final String sqlQuery = """
                INSERT INTO public.users (EMAIL, PASSWORD)
                VALUES (?, ?)
                """;

        KeyHolder generatedId = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});

            stmt.setString(1, newUserDto.email());
            stmt.setString(2, newUserDto.password());

            return stmt;
        }, generatedId);

        log.info(MESSAGE, newUserDto.email());

        return User.builder()
                .id(Objects.requireNonNull(generatedId.getKey()).longValue())
                .email(newUserDto.email())
                .password(newUserDto.password())
                .build();
    }

    @Override
    public User updateUserAccount(Long userId, UpdateUserAccount updateUserDto) {

        final String checkQuery = """
                SELECT *
                FROM public.users
                WHERE id = ?
                """;

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, userId);

        if (!userRows.next()) {
            log.warn(MESSAGE_ID, userId);
            throw new ObjectNotFoundException("User not present in Db!");
        }

        final String sqlQuery = """
                UPDATE public.users  SET PASSWORD = ?
                WHERE public.users.id = ?
                """;

        jdbcTemplate.update(sqlQuery, updateUserDto.password(), userId);

        log.info(MESSAGE_ID, userId);
        return getUserById(userId);
    }

    @Override
    public User deleteUserAccount(Long userId) {

        final String sqlQuery = """
                DELETE FROM users WHERE users.id = ?
                """;
        final User userToDelete = this.getUserById(userId);

        jdbcTemplate.update(sqlQuery, userId);

        log.info("User with userId: %d was removed from Db!".formatted(userId));

        return userToDelete;
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {

        Long id = resultSet.getLong("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        return User
                .builder()
                .id(id)
                .email(email)
                .password(password)
                .build();
    }
}
