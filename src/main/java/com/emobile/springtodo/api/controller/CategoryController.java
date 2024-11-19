package com.emobile.springtodo.api.controller;

import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.api.request.validation.Patterns;
import com.emobile.springtodo.api.swagger.ICategoryController;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Validated
public class CategoryController implements ICategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{categoryid}")
    public ResponseEntity<?> getCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID) {
        return ResponseEntity.ok(categoryService.getById(categoryID));
    }

    @GetMapping("/account/{accountid}")
    public ResponseEntity<?> getAllCategoriesByAccount(@Pattern(regexp = Patterns.ID, message = "Invalid account ID format") @PathVariable("accountid") String accountID) {
        return ResponseEntity.ok(categoryService.getByAccountId(accountID));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryCreationRequest request) {
        categoryService.createCategory(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{categoryid}")
    public ResponseEntity<?> updateCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID,
                                            @Valid @RequestBody CategoryUpdateRequest request) {
        if (request.categoryTitle() == null && request.categoryColour() == null) {
            return ResponseEntity.badRequest().body("Request body cannot be empty");
        }

        categoryService.updateCategory(categoryID, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{categoryid}")
    public ResponseEntity<?> deleteCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format")
                                                @PathVariable("categoryid") String categoryID) {
        categoryService.deleteCategory(categoryID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
