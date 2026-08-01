package com.expense.tracker.repository;

import com.expense.tracker.model.Category;
import com.expense.tracker.model.Expense;

import java.util.List;
import java.util.Optional;


public interface ExpenseRepository {

    // CREATE / SAVE expense
    Expense save(Expense expense);

    // RETRIEVE all expenses
    List<Expense> findAll();

    // FIND expense by id
    Optional<Expense> findById(Long id);

    // FILTER expenses by category
    List<Expense> filterByCategory(Category category);

    // DELETE an expense
    boolean deleteById(Long id);

    // Used for Integration Testing
    void clear();

}
