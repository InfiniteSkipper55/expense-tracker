package com.skipper.expensetracker.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Endpoint to create a new category

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Endpoint to edit an existing category

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Endpoint to retrieve a category by ID

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    // Endpoint to update an existing category

    @Override
    public Category updateCategory(Long categoryId, Category updatedCategory) {
        // Check if the category exists
        Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryId);
        if (existingCategoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }

        // Update existing category with new details
        Category existingCategory = existingCategoryOptional.get();
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    // Endpoint to delete an existing category

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
