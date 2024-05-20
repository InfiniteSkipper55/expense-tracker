package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.entities.Expense;

import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);

    Expense editExpense(Long expenseId, Expense updatedExpense);

    Boolean deleteExpense(Long expenseId);

    List<Expense> getExpensesByUserId(Long userId);

    List<Expense> getAllExpenses();

    List<Expense> getExpensesByCategory(Category category);

}
