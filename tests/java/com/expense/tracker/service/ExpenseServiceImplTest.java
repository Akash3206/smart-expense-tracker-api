package com.expense.tracker.service;

import com.expense.tracker.mapper.ExpenseMapper;
import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.expense.tracker.dto.CreateExpenseRequest;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Category;
import com.expense.tracker.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    private ExpenseRepository expenseRepository;
    private ExpenseMapper expenseMapper;
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {
        expenseRepository = Mockito.mock(ExpenseRepository.class);
        expenseMapper = new ExpenseMapper();
        expenseService = new ExpenseServiceImpl(
                expenseMapper,
                expenseRepository
        );
    }

    @Test
    void shouldAddExpense() {
        CreateExpenseRequest request = new CreateExpenseRequest(
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense savedExpense = new Expense(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(savedExpense);

        ExpenseResponse response = expenseService.addExpense(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Lunch", response.getTitle());
        assertEquals(new BigDecimal("250.00"), response.getAmount());
        assertEquals(Category.FOOD, response.getCategory());
        assertEquals(LocalDate.of(2026, 7, 31), response.getDate());

        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void shouldGetAllExpenses() {

        Expense expense1 = new Expense(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense expense2 = new Expense(
                2L,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        when(expenseRepository.findAll())
                .thenReturn(List.of(expense1, expense2));

        List<ExpenseResponse> responses = expenseService.getAllExpenses();

        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("Lunch", responses.get(0).getTitle());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("Bus Ticket", responses.get(1).getTitle());

        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void shouldGetExpensesByCategory() {

        Expense food1 = new Expense(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense food2 = new Expense(
                2L,
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 30)
        );

        when(expenseRepository.filterByCategory(Category.FOOD))
                .thenReturn(List.of(food1, food2));

        List<ExpenseResponse> responses = expenseService.getExpensesByCategory(Category.FOOD);

        assertEquals(2, responses.size());
        assertEquals(Category.FOOD, responses.get(0).getCategory());
        assertEquals(Category.FOOD, responses.get(1).getCategory());

        verify(expenseRepository, times(1)).filterByCategory(Category.FOOD);
    }

    @Test
    void shouldCalculateTotalExpenses() {

        Expense expense1 = new Expense(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense expense2 = new Expense(
                2L,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        when(expenseRepository.findAll())
                .thenReturn(List.of(expense1, expense2));

        BigDecimal total = expenseService.getTotalExpenses();

        assertEquals(new BigDecimal("325.00"), total);

        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnZeroWhenNoExpensesExist() {

        when(expenseRepository.findAll())
                .thenReturn(List.of());

        BigDecimal total = expenseService.getTotalExpenses();

        assertEquals(BigDecimal.ZERO, total);

        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void shouldCalculateTotalExpensesByCategory() {

        Expense food1 = new Expense(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense food2 = new Expense(
                2L,
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 30)
        );

        when(expenseRepository.filterByCategory(Category.FOOD))
                .thenReturn(List.of(food1, food2));

        BigDecimal total = expenseService.getTotalExpensesByCategory(Category.FOOD);

        assertEquals(new BigDecimal("1500.50"), total);

        verify(expenseRepository, times(1))
                .filterByCategory(Category.FOOD);
    }

    @Test
    void shouldReturnZeroWhenCategoryHasNoExpenses() {

        when(expenseRepository.filterByCategory(Category.HEALTH))
                .thenReturn(List.of());

        BigDecimal total = expenseService.getTotalExpensesByCategory(Category.HEALTH);

        assertEquals(BigDecimal.ZERO, total);

        verify(expenseRepository, times(1))
                .filterByCategory(Category.HEALTH);
    }

    @Test
    void shouldDeleteExpense() {

        Long id = 1L;

        when(expenseRepository.deleteById(id))
                .thenReturn(true);

        expenseService.deleteExpense(id);

        verify(expenseRepository, times(1))
                .deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingExpense() {

        Long id = 999L;

        when(expenseRepository.deleteById(id))
                .thenReturn(false);

        ExpenseNotFoundException exception = assertThrows(
                        ExpenseNotFoundException.class,
                        () -> expenseService.deleteExpense(id)
                );

        assertEquals(
                "Expense not found with id: 999",
                exception.getMessage()
        );

        verify(expenseRepository, times(1))
                .deleteById(id);
    }
}