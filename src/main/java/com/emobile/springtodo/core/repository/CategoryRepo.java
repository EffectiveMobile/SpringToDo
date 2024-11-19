package com.emobile.springtodo.core.repository;

import com.emobile.springtodo.core.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepo {

    List<Category> findAll();
    Optional<Category> findById(UUID id);
    List<Category> findByAccountId(UUID accountId);
    int save(Category category);
    int update(Category category, Long currentVersion);
    int delete(UUID categoryId);

}
