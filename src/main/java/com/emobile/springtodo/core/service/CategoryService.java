package com.emobile.springtodo.core.service;

import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.core.DAO.CategoryDAO;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.exception.customexceptions.CategoryCreationException;
import com.emobile.springtodo.core.exception.customexceptions.CategoryDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryDAO categoryDAO;

    private static final String CACHE_ONE_ENTRY = "category";
    private static final String CACHE_LIST_ENTRIES = "category_collection";

    private final RedisCacheManager cacheManager;


    public List<Category> findAll() {
        log.debug("Getting all categories");
        return categoryDAO.getAll();
    }

    @Cacheable(value = CACHE_ONE_ENTRY, key = "#categoryID", unless = "#result == null")
    public Category getById(String categoryID) {
        log.debug("Getting category with ID: {}", categoryID);
        return categoryDAO.getById(UUID.fromString(categoryID));
    }

    @Cacheable(value = CACHE_LIST_ENTRIES, key = "'account:' + #accountID", unless = "#result.isEmpty()")
    public List<Category> getByAccountId(String accountID) {
        log.debug("Getting categories for account ID: {}", accountID);
        return categoryDAO.getByAccountId(UUID.fromString(accountID));
    }

    @Transactional
    @CachePut(value = CACHE_ONE_ENTRY, key = "#result.categoryID", unless = "#result == null")
    public Category createCategory(CategoryCreationRequest request) throws CategoryCreationException {
        log.debug("Creating new category with title: {}", request.categoryTitle());

        Category category = Category.builder()
                .categoryID(UUID.randomUUID())
                .categoryTitle(request.categoryTitle())
                .categoryColour(request.categoryColour() != null ? request.categoryColour().toUpperCase() : "#FFDC74")
                .accountID(UUID.fromString(request.accountID()))
                .build();

        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setVersion(1L);

        try {
            categoryDAO.createCategory(category);
            return category;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create category due to data integrity violation", e);
            throw new CategoryCreationException("Failed to create category: " + e.getMessage());
        }
    }

    @Transactional
    @Caching(
            evict = {
            @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'account:' + #result.accountID", condition = "#result != null")},

            put = {
                    @CachePut(value = CACHE_ONE_ENTRY, key = "#categoryID", unless = "#result == null")
            }
    )
    public Category updateCategory(String categoryID, CategoryUpdateRequest request) {
        log.debug("Updating category with ID: {}", categoryID);

        Category category = categoryDAO.getById(UUID.fromString(categoryID));

        if (request.categoryTitle() != null) {
            category.setCategoryTitle(request.categoryTitle());
        }

        if (request.categoryColour() != null) {
            category.setCategoryColour(request.categoryColour().toUpperCase());
        }

        category.setUpdatedAt(LocalDateTime.now());

        Long currentVersion = category.getVersion();
        category.setVersion(category.getVersion() + 1);

        try {
            categoryDAO.updateCategory(category, currentVersion);
            return category;
        } catch (OptimisticLockingFailureException e) {
            log.error("Concurrent modification detected for category: {}", categoryID, e);
            throw new ConcurrentModificationException("Category was modified by another transaction");
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_ONE_ENTRY, key = "#categoryID"),
            @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'account:' + #result.accountID", condition = "#result != null")
    })
    public Category deleteCategory(String categoryID) throws CategoryDeletionException {
        log.debug("Deleting category with ID: {}", categoryID);

        Category category = categoryDAO.getById(UUID.fromString(categoryID));

        try {
            categoryDAO.deleteCategory(category.getCategoryID());
            return category;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete category due to data integrity violation", e);
            throw new CategoryDeletionException("Failed to delete category: " + e.getMessage());
        }
    }
}
