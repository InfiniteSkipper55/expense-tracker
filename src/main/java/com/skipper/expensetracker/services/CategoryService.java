package com.skipper.expensetracker.services;

import java.util.List;

import com.skipper.expensetracker.entities.Category;

public interface CategoryService {

    Category addCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(Long categoryId);

    Category updateCategory(Long categoryId, Category updatedCategory);

    void deleteCategory(Long categoryId);

}
