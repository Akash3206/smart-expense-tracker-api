package com.expense.tracker.service;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.model.Category;
import com.expense.tracker.model.Expense;
import com.expense.tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.expense.tracker.mapper.ExpenseMapper;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements  ExpenseService {

    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponse addExpense(CreateExpenseRequest request) {

        // Convert CreateExpenseRequest(DTO Object) to Expense
        Expense expense = expenseMapper.toEntity(request);

        // Add the expense
        Expense savedExpense = expenseRepository.save(expense);

        // Convert it into ExpenseResponse(DTO object) and return
        return expenseMapper.toResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        // TODO - Retrieve All Expenses and Convert them into List of ExpenseRepsonse
        return expenseRepository.findAll()
                .stream()
                .map(expenseMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByCategory(Category category) {
        // TODO - Retrieve All Expenses by Category and Convert them into List of ExpenseRepsonse
        return expenseRepository.filterByCategory(category)
                .stream()
                .map(expenseMapper::toResponse)
                .toList();
    }

    @Override
    public BigDecimal getTotalExpenses() {
        // TODO - Retrieve All Expenses and Add the amount of all Expense and Convert it to BigDecimal
        return expenseRepository.findAll()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpensesByCategory(Category category) {
        // TODO - Retrieve All Expenses by Category and Add the amount of all Expense and Convert it to BigDecimal
        return expenseRepository.filterByCategory(category)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteExpense(Long id) {
        boolean isDeleted = expenseRepository.deleteById(id);

        if(!isDeleted) {
            throw new ExpenseNotFoundException(id);
        }
    }
}
