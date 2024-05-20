package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.Expense;

import java.util.Date;
import java.util.List;

public interface ReportService {
    List<Expense> generateExpenseReport(Long userId, Date startDate, Date endDate);

    Double calculateTotalExpenses(Long userId, Date startDate, Date endDate);
}
