package com.skipper.expensetracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skipper.expensetracker.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
