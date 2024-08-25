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
        Expense expense = new Expense();
        expense.setUser(new User());
        expense.setAmount(100.0);
        expense.setDescription("Test Expense");
        expense.setCategory(new Category());
        expense.setDate(new Date());

        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense result = expenseService.addExpense(expense);

        Assertions.assertEquals(expense, result);
        Mockito.verify(expenseRepository, Mockito.times(1)).save(expense);
    }

    @Test
    public void testAddExpense_NullExpenseRecord() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(null);
        }, "Expense record fields cannot be null");
    }

    @Test
    public void testAddExpense_ExpenseRecordWithNullFields() {
        Expense expense = new Expense();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(expense);
        }, "Expense record fields cannot be null");
    }

    @Test
    public void testAddExpense_ExpenseRecordWithNullUser() {
        Expense expense = new Expense();
        expense.setAmount(100.0);
        expense.setDescription("Test Expense");
        expense.setCategory(new Category());
        expense.setDate(new Date());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(expense);
        }, "Expense record fields cannot be null");
    }

    @Test
    void testGetAllExpenses() {
        List<Expense> mockExpenses = List.of(
                new Expense(1L, null, null, null, "Expense 1", null),
                new Expense(2L, null, null, null, "Expense 2", null));
        when(expenseRepository.findAll()).thenReturn(mockExpenses);

        List<Expense> result = expenseService.getAllExpenses();

        assertEquals(2, result.size());
    }

}