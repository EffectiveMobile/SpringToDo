package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
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

    String sqlQuery = """
                   SELECT *
                   FROM public.users
                   OFFSET :?
                   LIMIT :?
                   """;

        return jdbcTemplate.query(sqlQuery, this::makeUser, from, size);
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
