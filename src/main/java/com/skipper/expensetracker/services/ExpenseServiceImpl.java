package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.repositories.ExpenseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    public Expense addExpense(Expense expense) {
        // Validate expense record
        if (expense.getUser() == null || expense.getAmount() == null || expense.getDescription() == null
                || expense.getCategory() == null || expense.getDate() == null) {
            throw new IllegalArgumentException("Expense record fields cannot be null");
        }
        return expenseRepository.save(expense);
    }

    @Override
    public Expense editExpense(Long expenseId, Expense updatedExpense) {
        // Validate update expense record
        if (updatedExpense == null || updatedExpense.getUser() == null
                || updatedExpense.getAmount() == null || updatedExpense.getDescription() == null
                || updatedExpense.getCategory() == null || updatedExpense.getDate() == null) {
            throw new IllegalArgumentException("Update Expense record fields cannot be null");
        }

        // Check if the expense record exists
        Optional<Expense> existingExpenseOptional = expenseRepository.findById(expenseId);
        if (existingExpenseOptional.isEmpty()) {
            throw new IllegalArgumentException("Expense record not found");
        }

        Expense existingExpense = existingExpenseOptional.get();

        // Update existing expense with new details
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setUser(updatedExpense.getUser());

        return expenseRepository.save(existingExpense);
    }

    @Override
    public Boolean deleteExpense(Long expenseId) {
        // Check if the expense record exists
        Optional<Expense> existingExpenseOptional = expenseRepository.findById(expenseId);
        if (existingExpenseOptional.isEmpty()) {
            return false;
        }

        expenseRepository.deleteById(expenseId);
        return true;
    }

    @Override
    public Expense getExpensesByExpenseId(Long expenseId) {
        return expenseRepository.findById(expenseId).orElse(null);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

}
