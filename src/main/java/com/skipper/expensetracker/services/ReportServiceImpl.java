package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.repositories.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ExpenseRepository expenseRepository;

    public ReportServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Generate expense report for a user within a date range
    @Override
    public List<Expense> generateExpenseReport(Long userId, Date startDate, Date endDate) {
        // Validate user ID
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Validate date range
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        return expenseRepository.findByUserUserIdAndDateBetween(userId, startDate, endDate);
    }

    // Calculate total expenses for a user within a date range
    @Override
    public Double calculateTotalExpenses(Long userId, Date startDate, Date endDate) {
        // Validate user ID
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Validate date range
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        List<Expense> expenses = expenseRepository.findByUserUserIdAndDateBetween(userId, startDate, endDate);
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }
}
