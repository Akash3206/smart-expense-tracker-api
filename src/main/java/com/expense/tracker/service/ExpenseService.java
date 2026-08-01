package com.expense.tracker.service;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Category;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {

    // ADD expense
    ExpenseResponse addExpense(CreateExpenseRequest expense);

    // RETRIEVE all expenses
    List<ExpenseResponse> getAllExpenses();

    // GET expenses by category
    List<ExpenseResponse> getExpensesByCategory(Category category);

    // GET total expenses
    BigDecimal getTotalExpenses();

    // GET total expenses by category
    BigDecimal getTotalExpensesByCategory(Category category);

    // DELETE an expense by id
    void deleteExpense(Long id);

}
