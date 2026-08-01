package com.expense.tracker.controller;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Category;
import com.expense.tracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse response = expenseService.addExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpenseByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalExpenses() {
        return ResponseEntity.ok(expenseService.getTotalExpenses());
    }

    @GetMapping("/total/category/{category}")
    public ResponseEntity<BigDecimal> getTotalExpensesByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(expenseService.getTotalExpensesByCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
