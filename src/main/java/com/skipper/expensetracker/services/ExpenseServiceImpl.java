package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.entities.User;
import com.skipper.expensetracker.repositories.ExpenseRepository;
import com.skipper.expensetracker.repositories.UserRepository;

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

    private final UserRepository userRepository;

    // Endpoint to create a new expense
    @Override
    public Expense addExpense(Expense expense) {
        // Validate expense record
        if (expense == null || expense.getUser() == null
                || expense.getAmount() == null || expense.getDescription() == null
                || expense.getCategory() == null || expense.getDate() == null) {
            throw new IllegalArgumentException("Expense record fields cannot be null");
        }
        return expenseRepository.save(expense);
    }

    // Endpoint to edit an existing expense
    @Override
    public Expense editExpense(Long expenseId, Expense updatedExpense) {
        // Validate expense ID
        if (expenseId == null) {
            throw new IllegalArgumentException("Expense id cannot be null");
        }

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
        return expenseRepository.save(existingExpense);
    }

    // Endpoint to delete an existing expense
    @Override
    public Boolean deleteExpense(Long expenseId) {
        // Validate expense ID
        if (expenseId == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }

        // Check if the expense record exists
        Optional<Expense> existingExpenseOptional = expenseRepository.findById(expenseId);
        if (existingExpenseOptional.isEmpty()) {
            return false;
        }

        expenseRepository.deleteById(expenseId);
        return true;
    }

    // Endpoint to retrieve all expenses for a user
    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        // Validate user ID
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Check if user record exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User record not found");
        }

        return expenseRepository.findByUserUserId(userId);
    }

    // Endpoint to retrieve all expenses
    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Endpoint to retrieve expenses by category
    @Override
    public List<Expense> getExpensesByCategory(Category category) {
        return expenseRepository.findByCategory(category);
    }
}
