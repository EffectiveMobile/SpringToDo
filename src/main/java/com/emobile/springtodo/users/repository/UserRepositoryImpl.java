package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.utils.exception.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

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

        SqlRowSet userRows =  jdbcTemplate.queryForRowSet(sqlQuery, userId);

        if (!userRows.next()) {
            log.warn("User with userId: {} are not present in Db!", userId);
            throw new ObjectNotFoundException("User not present in Db!");
        }

        final String checkQuery = """
                      SELECT *
                      FROM public.users
                      WHERE users.id = ?
                                  """;

        log.info("User with userId: {} was sent", userId);
        return jdbcTemplate.queryForObject(checkQuery, this::makeUser, userId);
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
