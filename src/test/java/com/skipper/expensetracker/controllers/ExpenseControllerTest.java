package com.skipper.expensetracker.controllers;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    public void setup() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateExpense_ValidInput() {
        // Arrange
        Expense validExpense = new Expense();
        
        when(expenseService.addExpense(any(Expense.class))).thenReturn(validExpense);

        // Act
        ResponseEntity<Expense> response = expenseController.createExpense(validExpense);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(validExpense, response.getBody());
    }

    @Test
    public void testCreateExpense_NullInput() {
        // Arrange
        Expense nullExpense = null;

        // Act
        ResponseEntity<Expense> response = expenseController.createExpense(nullExpense);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}