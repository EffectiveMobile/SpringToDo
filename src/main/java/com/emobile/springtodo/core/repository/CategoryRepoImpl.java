package com.emobile.springtodo.core.repository;

import com.emobile.springtodo.core.DTO.CategoryMapper;
import com.emobile.springtodo.core.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepoImpl implements CategoryRepo {

    private final NamedParameterJdbcTemplate template;
    private static final RowMapper<Category> CATEGORY_MAPPER = new CategoryMapper();

    private static final String FIND_ALL = "SELECT category_id, title, colour, account_id, created_at, updated_at, version FROM category";
    private static final String FIND_BY_ID = "SELECT category_id, title, colour, account_id, created_at, updated_at, version FROM category WHERE category_id = :id";
    private static final String FIND_BY_ACCOUNT_ID = "SELECT category_id, title, colour, account_id, created_at, updated_at, version FROM category WHERE account_id = :accountId";
    private static final String SAVE_CATEGORY = """
            INSERT INTO category (category_id, title, colour, account_id, created_at, updated_at, version)
            VALUES (:categoryId, :title, :colour, :accountId, :createdAt, :updatedAt, :version)
            """;
    private static final String UPDATE_CATEGORY = """
            UPDATE category SET title = :title, colour = :colour, updated_at = :updatedAt, version = :version
            WHERE category_id = :categoryId AND version = :currentVersion
            """;
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE category_id = :categoryId";

    @Override
    public List<Category> findAll() {
        return template.query(FIND_ALL, CATEGORY_MAPPER);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(template.queryForObject(FIND_BY_ID, params, CATEGORY_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findByAccountId(UUID accountId) {
        SqlParameterSource params = new MapSqlParameterSource("accountId", accountId);
        return template.query(FIND_BY_ACCOUNT_ID, params, CATEGORY_MAPPER);
    }

    @Override
    public int save(Category category) {
        MapSqlParameterSource params = createParameterSource(category);
        return template.update(SAVE_CATEGORY, params);
    }

    @Override
    public int update(Category category, Long currentVersion) {
        MapSqlParameterSource params = createParameterSource(category);
        params.addValue("currentVersion", currentVersion);
        return template.update(UPDATE_CATEGORY, params);
    }

    @Override
    public int delete(UUID categoryId) {
        SqlParameterSource params = new MapSqlParameterSource("categoryId", categoryId);
        return template.update(DELETE_CATEGORY, params);
    }

    private MapSqlParameterSource createParameterSource(Category category) {
        return new MapSqlParameterSource()
                .addValue("categoryId", category.getCategoryID())
                .addValue("title", category.getCategoryTitle())
                .addValue("colour", category.getCategoryColour())
                .addValue("accountId", category.getAccountID())
                .addValue("createdAt", category.getCreatedAt())
                .addValue("updatedAt", category.getUpdatedAt())
                .addValue("version", category.getVersion());
    }
}
