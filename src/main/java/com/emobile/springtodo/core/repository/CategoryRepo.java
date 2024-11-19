package com.emobile.springtodo.core.repository;

import com.emobile.springtodo.core.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepo {

    public List<Category> findAll();
    public Optional<Category> findById(UUID id);
    public List<Category> findByAccountId(UUID accountId);
    public int save(Category category);
    public int update(Category category, Long currentVersion);
    public int delete(UUID categoryId);

}
