package com.skipper.expensetracker.controllers;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExpense_NullExpense_ReturnsBadRequest() {
        // Act
        ResponseEntity<Expense> result = expenseController.createExpense(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testCreateExpense_ValidExpense_ReturnsCreatedExpense() throws Exception {
        // Arrange
        Expense expense = new Expense();
        Expense createdExpense = new Expense();
        when(expenseService.addExpense(any(Expense.class))).thenReturn(createdExpense);

        // Act
        ResponseEntity<Expense> result = expenseController.createExpense(expense);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(createdExpense, result.getBody());
    }

    @Test
    void testCreateExpense_IllegalArgumentException_ReturnsBadRequest() throws Exception {
        // Arrange
        Expense expense = new Expense();
        when(expenseService.addExpense(any(Expense.class))).thenThrow(IllegalArgumentException.class);

        // Act
        ResponseEntity<Expense> result = expenseController.createExpense(expense);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testGetAllExpenses_EmptyList_ReturnsNoContent() {
        // Arrange
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testGetAllExpenses_NonEmptyList_ReturnsOkWithExpenses() {
        // Arrange
        List<Expense> expenses = List.of(new Expense(1L, null, null, null, "Expense 1", null));
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        // Act
        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expenses, result.getBody());
    }

    @Test
    void testGetAllExpenses_IllegalArgumentException_ReturnsNotFound() {
        // Arrange
        when(expenseService.getAllExpenses()).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetExpensesByUser_ValidUserId() {
        // Arrange
        Long userId = 1L;
        List<Expense> expectedExpenses = Arrays.asList(new Expense(1L, null, null, null, "Expense 1", null));
        when(expenseService.getExpensesByUserId(userId)).thenReturn(expectedExpenses);

        // Act
        ResponseEntity<List<Expense>> result = expenseController.getExpensesByUser(userId);

        // Assert
        assertEquals(ResponseEntity.ok().body(expectedExpenses), result);
    }

    @Test
    void testGetExpensesByUser_NullUserId() {
        // Act
        ResponseEntity<List<Expense>> result = expenseController.getExpensesByUser(null);

        // Assert
        assertEquals(ResponseEntity.badRequest().build(), result);
    }

    @Test
    void testGetExpensesByUser_UserIdNotFound() {
        // Arrange
        Long userId = 2L;
        Mockito.when(expenseService.getExpensesByUserId(userId)).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<Expense>> result = expenseController.getExpensesByUser(userId);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), result);
    }

}
