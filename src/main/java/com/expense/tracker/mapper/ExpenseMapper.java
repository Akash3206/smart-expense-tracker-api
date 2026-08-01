package com.expense.tracker.mapper;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public Expense toEntity(CreateExpenseRequest request) {
        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        return expense;
    }

    public ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse expenseResponse = new ExpenseResponse();

        expenseResponse.setId(expense.getId());
        expenseResponse.setTitle(expense.getTitle());
        expenseResponse.setAmount(expense.getAmount());
        expenseResponse.setCategory(expense.getCategory());
        expenseResponse.setDate(expense.getDate());

        return expenseResponse;
    }
}
