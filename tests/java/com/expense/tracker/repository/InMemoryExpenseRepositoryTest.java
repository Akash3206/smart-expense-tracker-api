package com.expense.tracker.repository;

import com.expense.tracker.model.Category;
import com.expense.tracker.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryExpenseRepositoryTest {

    private InMemoryExpenseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExpenseRepository();
    }

    // Testing save
    @Test
    void shouldSaveExpense() {
        Expense expense = new Expense(
                null,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense savedExpense = repository.save(expense);

        assertNotNull(savedExpense);
        assertNotNull(savedExpense.getId());
        assertEquals(1L, savedExpense.getId());
        assertEquals("Lunch", savedExpense.getTitle());
        assertEquals(new BigDecimal("250.00"), savedExpense.getAmount());
        assertEquals(Category.FOOD, savedExpense.getCategory());
        assertEquals(LocalDate.of(2026, 7, 31), savedExpense.getDate());

        assertTrue(repository.findById(savedExpense.getId()).isPresent());
    }

    // Testing find all Expenses
    @Test
    void shouldFindAllExpense() {

        Expense expense1 = new Expense(
                null,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense expense2 = new Expense(
                null,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        repository.save(expense1);
        repository.save(expense2);

        List<Expense> expenses = repository.findAll();

        assertEquals(2, expenses.size());
        assertTrue(expenses.contains(expense1));
        assertTrue(expenses.contains(expense2));
    }

    // Testing Find Expense by id
    @Test
    void shouldFindExpenseById() {

        Expense expense = new Expense(
                null,
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 30)
        );

        Expense savedExpense = repository.save(expense);

        Optional<Expense> foundExpense = repository.findById(savedExpense.getId());

        assertTrue(foundExpense.isPresent());
        assertEquals(savedExpense, foundExpense.get());
    }

    @Test
    void shouldReturnEmptyWhenExpenseIdDoesNotExist() {
        Optional<Expense> foundExpense = repository.findById(999L);

        assertTrue(foundExpense.isEmpty());
    }

    @Test
    void shouldFilterExpensesByCategory() {

        Expense food1 = new Expense(
                null,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        Expense food2 = new Expense(
                null,
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 30)
        );

        Expense transport = new Expense(
                null,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        repository.save(food1);
        repository.save(food2);
        repository.save(transport);

        List<Expense> foodExpenses = repository.filterByCategory(Category.FOOD);

        assertEquals(2, foodExpenses.size());
        assertTrue(foodExpenses.contains(food1));
        assertTrue(foodExpenses.contains(food2));
        assertFalse(foodExpenses.contains(transport));
    }

    @Test
    void shouldReturnEmptyListWhenCategoryHasNoExpenses() {

        Expense expense = new Expense(
                null,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        repository.save(expense);

        List<Expense> foodExpenses = repository.filterByCategory(Category.FOOD);

        assertTrue(foodExpenses.isEmpty());
    }

    @Test
    void shouldDeleteExpenseById() {

        Expense expense = new Expense(
                null,
                "Movie",
                new BigDecimal("350.00"),
                Category.ENTERTAINMENT,
                LocalDate.of(2026, 7, 29)
        );

        Expense savedExpense = repository.save(expense);

        boolean deleted = repository.deleteById(savedExpense.getId());

        assertTrue(deleted);
        assertTrue(repository.findById(savedExpense.getId()).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingExpense() {

        boolean deleted = repository.deleteById(999L);

        assertFalse(deleted);
    }
}