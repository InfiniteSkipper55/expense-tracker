package com.skipper.expensetracker.controllers;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

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
    void testCreateExpense_NullExpense() {
        ResponseEntity<Expense> result = expenseController.createExpense(null);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testCreateExpense_ValidExpense() throws Exception {
        Expense expense = new Expense();
        Expense createdExpense = new Expense();
        when(expenseService.addExpense(expense)).thenReturn(createdExpense);

        ResponseEntity<Expense> result = expenseController.createExpense(expense);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(createdExpense, result.getBody());
    }

    @Test
    void testGetAllExpenses_EmptyList() {
        when(expenseService.getAllExpenses()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testGetAllExpenses_NonEmptyList() {
        List<Expense> expenses = List.of(new Expense(1L, null, null, null, "Expense 1", null));
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expenses, result.getBody());
    }

    @Test
    void testGetAllExpenses_IllegalArgumentException() {
        when(expenseService.getAllExpenses()).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<Expense>> result = expenseController.getAllExpenses();

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetExpensesByExpenseId_ExpenseIdIsNull() {
        Long expenseId = null;

        ResponseEntity<Expense> response = expenseController.getExpensesByExpenseId(expenseId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetExpensesByExpenseId_ExpenseIdIsValid() {
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        when(expenseService.getExpensesByExpenseId(expenseId)).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.getExpensesByExpenseId(expenseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    void testGetExpensesByExpenseId_ExpenseIdIsValid_ThrowsIllegalArgumentException() {
        Long expenseId = 1L;

        when(expenseService.getExpensesByExpenseId(expenseId)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<Expense> response = expenseController.getExpensesByExpenseId(expenseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateExpense_InvalidInput() {
        Long expenseId = 1L;
        Expense updatedExpense = null;

        ResponseEntity<Expense> response = expenseController.updateExpense(expenseId, updatedExpense);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateExpense_ValidInput() throws Exception {
        Long expenseId = 1L;
        Expense updatedExpense = new Expense();
        Expense expectedExpense = new Expense();
        when(expenseService.editExpense(expenseId, updatedExpense)).thenReturn(expectedExpense);

        ResponseEntity<Expense> response = expenseController.updateExpense(expenseId, updatedExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedExpense, response.getBody());
    }

    @Test
    void testUpdateExpense_ExpenseNotFound() throws Exception {
        Long expenseId = 1L;
        Expense updatedExpense = new Expense();
        when(expenseService.editExpense(expenseId, updatedExpense)).thenThrow(new IllegalArgumentException("Expense not found"));

        ResponseEntity<Expense> response = expenseController.updateExpense(expenseId, updatedExpense);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteExpense_ValidExpenseId() {
        Long expenseId = 1L;
        when(expenseService.deleteExpense(expenseId)).thenReturn(true);

        ResponseEntity<Void> result = expenseController.deleteExpense(expenseId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testDeleteExpense_NullExpenseId() {
        ResponseEntity<Void> result = expenseController.deleteExpense(null);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verifyNoInteractions(expenseService);
    }

    @Test
    void testDeleteExpense_ExpenseNotFound() {
        Long expenseId = 1L;
        when(expenseService.deleteExpense(expenseId)).thenReturn(false);

        ResponseEntity<Void> result = expenseController.deleteExpense(expenseId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    
}
