package com.skipper.expensetracker.repositories;

import com.skipper.expensetracker.entities.Category;
import com.skipper.expensetracker.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Custom query method to find expense records by user ID
    List<Expense> findByUserUserId(Long userId);

    // Custom query method to find expense records by category
    List<Expense> findByCategory(Category category);

    // Custom query method to find expense records by date range
    List<Expense> findByDateBetween(Date startDate, Date endDate);

    // Custom JPQL query to find expense records by user ID and category
    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId AND e.category = :category")
    List<Expense> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") Category category);

    // Custom native SQL query to find total expenses by user ID
    @Query(value = "SELECT SUM(amount) FROM expenses WHERE user_id = :userId", nativeQuery = true)
    Double getTotalExpensesByUserId(@Param("userId") Long userId);

    List<Expense> findByUserUserIdAndDateBetween(Long userId, Date startDate, Date endDate);
}
