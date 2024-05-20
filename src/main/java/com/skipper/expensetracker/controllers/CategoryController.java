package com.skipper.expensetracker.controllers;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // Endpoint to create a new category
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        try {
            if (category == null) {
                return ResponseEntity.badRequest().build();
            }
            Category savedCategory = categoryService.addCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to retrieve all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to retrieve a category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        if (categoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to update an existing category
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category updatedCategory) {
        if (categoryId == null || updatedCategory == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Category category = categoryService.updateCategory(categoryId, updatedCategory);
            if (category == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to delete an existing category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        if (categoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            categoryService.deleteCategory(categoryId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.noContent().build();
    }
}
