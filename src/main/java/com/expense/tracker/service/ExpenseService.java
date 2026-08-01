package com.expense.tracker.service;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Category;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {

    // ADD expense
    ExpenseResponse addExpense(CreateExpenseRequest expense);

    List<ExpenseResponse> getAllExpenses();

    List<ExpenseResponse> getExpensesByCategory(Category category);

    BigDecimal getTotalExpenses();

    BigDecimal getTotalExpensesByCategory(Category category);

    void deleteExpense(Long id);

}
