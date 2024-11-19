package com.emobile.springtodo.core.DAO;

import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.exception.customexceptions.CategoryNotFoundException;
import com.emobile.springtodo.core.exception.customexceptions.CustomDataAccessException;
import com.emobile.springtodo.core.repository.CategoryRepoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryDAO {

    private final CategoryRepoImpl categoryRepo;
    private static final String CATEGORY_NOT_FOUND = "Category with ID %s not found";
    private static final String CATEGORIES_BY_ACCOUNT_ERROR = "Error getting categories for account ID %s";

    public List<Category> getAll() {
        try {
            List<Category> categoryList = categoryRepo.findAll();
            if (categoryList.isEmpty()) {
                throw new CategoryNotFoundException("No categories found");
            }
            return categoryList;
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error getting all categories", e);
        }
    }

    public Category getById(UUID id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format(CATEGORY_NOT_FOUND, id)));
    }

    public List<Category> getByAccountId(UUID accountID) {
        try {
            List<Category> categoryList = categoryRepo.findByAccountId(accountID);
            if (categoryList.isEmpty()) {
                throw new CategoryNotFoundException("No categories found with Account ID " + accountID);
            }
            return categoryList;
        } catch (DataAccessException e) {
            throw new CustomDataAccessException(String.format(CATEGORIES_BY_ACCOUNT_ERROR, accountID), e);
        }
    }

    public void createCategory(Category category) {
        try {
            int rowsAffected = categoryRepo.save(category);
            if (rowsAffected == 0) {
                throw new CustomDataAccessException("Failed to save category: No rows affected");
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error saving category", e);
        }
    }

    public void updateCategory(Category category, Long currentVersion) {
        try {
            int rowsAffected = categoryRepo.update(category, currentVersion);
            if (rowsAffected == 0) {
                throw new CategoryNotFoundException(String.format("Failed to update category: " + CATEGORY_NOT_FOUND, category.getCategoryID()));
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error updating category", e);
        }
    }

    public void deleteCategory(UUID categoryID) {
        try {
            int rowsAffected = categoryRepo.delete(categoryID);
            if (rowsAffected == 0) {
                throw new CategoryNotFoundException(String.format("Failed to delete category: " + CATEGORY_NOT_FOUND, categoryID));
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error deleting category", e);
        }
    }
}
