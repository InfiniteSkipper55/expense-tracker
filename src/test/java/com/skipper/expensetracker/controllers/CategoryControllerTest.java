package com.skipper.expensetracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.services.CategoryService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryService mockCategoryServiceWithException() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        Mockito.when(categoryService.addCategory(Mockito.any(Category.class)))
               .thenThrow(new RuntimeException("Database error"));
        return categoryService;
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testAddCategory_NullCategory() {
        CategoryController controller = new CategoryController(null);
        ResponseEntity<Category> response = controller.addCategory(null);
        assertEquals("Expected BadRequest response", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    void testAddCategory_ValidCategory() {
        CategoryService mockService = mock(CategoryService.class);
        Category category = new Category();
        when(mockService.addCategory(any(Category.class))).thenReturn(category);
        
        CategoryController controller = new CategoryController(mockService);
        ResponseEntity<Category> response = controller.addCategory(category);
        
        assertEquals("Expected Created response with saved category", HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Expected saved category in response body", category, response.getBody());
    }
    
    @Test
    void testAddCategory_Exception() {
        CategoryController controller = new CategoryController(mockCategoryServiceWithException());
        Category category = new Category();
        ResponseEntity<Category> response = controller.addCategory(category);
        assertEquals("Expected InternalServerError response", HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    void testGetAllCategories_Success() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    void testGetAllCategories_NullCategories() {
        when(categoryService.getAllCategories()).thenReturn(null);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllCategories_Exception() {
        when(categoryService.getAllCategories()).thenThrow(new RuntimeException());

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetCategoryById_NullCategoryId() {
        ResponseEntity<Category> response = categoryController.getCategoryById(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetCategoryById_ValidCategoryId_ExistingCategory() {
        Category category = new Category();
        when(categoryService.getCategoryById(anyLong())).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void testGetCategoryById_ValidCategoryId_NonExistingCategory() {
        when(categoryService.getCategoryById(anyLong())).thenReturn(null);

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetCategoryById_ThrowsException() {
        when(categoryService.getCategoryById(anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateCategory_ValidInput() {
        Long categoryId = 1L;
        Category updatedCategory = new Category();
        updatedCategory.setCategoryName("Updated Category");

        Category expectedCategory = new Category();
        expectedCategory.setCategoryName("Updated Category");

        when(categoryService.updateCategory(categoryId, updatedCategory)).thenReturn(expectedCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCategory, response.getBody());
    }

    @Test
    void testUpdateCategory_NullCategoryIdOrUpdatedCategory() {
        Long categoryId = null;
        Category updatedCategory = new Category();

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        categoryId = 1L;
        updatedCategory = null;
        response = categoryController.updateCategory(categoryId, updatedCategory);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateCategory_CategoryNotFound() {
        Long categoryId = 1L;
        Category updatedCategory = new Category();

        when(categoryService.updateCategory(categoryId, updatedCategory)).thenReturn(null);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateCategory_Exception() {
        Long categoryId = 1L;
        Category updatedCategory = new Category();

        when(categoryService.updateCategory(categoryId, updatedCategory)).thenThrow(new RuntimeException());

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDeleteCategory_ValidCategoryId() {
        Long categoryId = 1L;

        ResponseEntity<Void> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void testDeleteCategory_NullCategoryId() {
        Long categoryId = null;

        ResponseEntity<Void> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteCategory_InternalServerError() {
        Long categoryId = 1L;
        doThrow(new RuntimeException()).when(categoryService).deleteCategory(categoryId);

        ResponseEntity<Void> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
}