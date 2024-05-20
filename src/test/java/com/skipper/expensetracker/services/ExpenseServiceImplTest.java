package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.entities.User;
import com.skipper.expensetracker.repositories.ExpenseRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Test
    public void testAddExpense_ValidExpenseRecord() {
        // Arrange
        Expense expense = new Expense();
        expense.setUser(new User());
        expense.setAmount(100.0);
        expense.setDescription("Test Expense");
        expense.setCategory(new Category());
        expense.setDate(new Date());

        // Mock the save method of the ExpenseRepository
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        Expense result = expenseService.addExpense(expense);

        // Assert
        Assertions.assertEquals(expense, result);
        Mockito.verify(expenseRepository, Mockito.times(1)).save(expense);
    }

    @Test
    public void testAddExpense_NullExpenseRecord() {

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(null);
        }, "Expense record fields cannot be null");
    }

    @Test
    public void testAddExpense_ExpenseRecordWithNullFields() {
        // Arrange
        Expense expense = new Expense();

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(expense);
        }, "Expense record fields cannot be null");
    }

    @Test
    public void testAddExpense_ExpenseRecordWithNullUser() {
        // Arrange
        Expense expense = new Expense();
        expense.setAmount(100.0);
        expense.setDescription("Test Expense");
        expense.setCategory(new Category());
        expense.setDate(new Date());

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(expense);
        }, "Expense record fields cannot be null");
    }

    @Test
    public void testEditExpense_ValidExpenseIdAndUpdatedExpenseRecord() {
        // Arrange
        Long expenseId = 1L;
        Category category = new Category(1L, "Category 1");
        User user = new User(expenseId, "User 1", null, null, null, null, null);
        Expense updatedExpense = new Expense(1L, category, user, 100.0, "Description", new Date());
        Expense existingExpense = new Expense(expenseId, category, user, 50.0, "Description", new Date());
        Mockito.when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));

        // Act
        Expense updatedExpenseResult = expenseService.editExpense(expenseId, updatedExpense);

        // Assert
        assertEquals(updatedExpense.getAmount(), updatedExpenseResult.getAmount());
        assertEquals(updatedExpense.getDescription(), updatedExpenseResult.getDescription());
        assertEquals(updatedExpense.getCategory(), updatedExpenseResult.getCategory());
        assertEquals(updatedExpense.getDate(), updatedExpenseResult.getDate());
        assertEquals(existingExpense.getExpenseId(), updatedExpenseResult.getExpenseId());
        assertEquals(existingExpense.getUser(), updatedExpenseResult.getUser());
        Mockito.verify(expenseRepository, Mockito.times(1)).findById(expenseId);
        Mockito.verify(expenseRepository, Mockito.times(1)).save(updatedExpenseResult);
    }



    @Test
    void testGetExpenseById() {
        // Arrange
        Expense mockExpense = new Expense();
        mockExpense.setExpenseId(1L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(mockExpense));

        // Act
        List<Expense> result = expenseService.getExpensesByUserId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getExpenseId());
    }

    @Test
    void testGetAllExpenses() {
        // Arrange
        List<Expense> mockExpenses = List.of(
                new Expense(1L, null, null, null, "Expense 1", null),
                new Expense(2L, null, null, null, "Expense 2", null));
        when(expenseRepository.findAll()).thenReturn(mockExpenses);

        // Act
        List<Expense> result = expenseService.getAllExpenses();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void testCreateExpense() {
        // Arrange
        Expense expenseToCreate = new Expense();
        expenseToCreate.setDescription("New Expense");

        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense createdExpense = invocation.getArgument(0);
            createdExpense.setExpenseId(1L); // Assign an ID to the created expense
            return createdExpense;
        });

        // Act
        Expense result = expenseService.addExpense(expenseToCreate);

        // Assert
        assertEquals(1L, result.getExpenseId());
        assertEquals("New Expense", result.getDescription());
    }

    @Test
    void testDeleteExpense() {
        // Arrange
        Long expenseIdToDelete = 1L;

        // Act
        expenseService.deleteExpense(expenseIdToDelete);

        // Assert
        verify(expenseRepository, times(1)).deleteById(expenseIdToDelete);
    }
}