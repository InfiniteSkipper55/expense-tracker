package com.skipper.expensetracker.controllers;

import com.skipper.expensetracker.entities.Expense;
import com.skipper.expensetracker.services.ExpenseService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;    

    // Endpoint to create a new expense
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        if (expense == null) {
            return ResponseEntity.badRequest().build();            
        }
        try {
            Expense createdExpense = expenseService.addExpense(expense);
            return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint to retrieve all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = expenseService.getAllExpenses();
            if (expenses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(expenses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to retrieve expenses for a user
    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpensesByExpenseId(@PathVariable Long expenseId) {
        if (expenseId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Expense expense = expenseService.getExpensesByExpenseId(expenseId);
            return ResponseEntity.ok(expense);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update an existing expense
    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long expenseId, @RequestBody Expense updatedExpense) {
        if (expenseId == null || updatedExpense == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Expense updated = expenseService.editExpense(expenseId, updatedExpense);
            return ResponseEntity.ok().body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint to delete an expense
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        if (expenseId == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = expenseService.deleteExpense(expenseId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
