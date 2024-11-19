package com.emobile.springtodo.core.DTO;

import com.emobile.springtodo.core.entity.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = Category.builder()
                .categoryID(UUID.fromString(rs.getString("category_id")))
                .categoryTitle(rs.getString("title"))
                .categoryColour(rs.getString("colour"))
                .accountID(UUID.fromString(rs.getString("account_id")))
                .build();

        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        category.setVersion(rs.getLong("version"));

        return category;
    }
}
