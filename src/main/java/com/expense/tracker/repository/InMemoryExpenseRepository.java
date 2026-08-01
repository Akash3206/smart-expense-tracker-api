package com.expense.tracker.repository;

import com.expense.tracker.model.Category;
import com.expense.tracker.model.Expense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {
    private final Map<Long, Expense> expenses;
    private final AtomicLong idGenerator;

    public InMemoryExpenseRepository() {
        this.expenses = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public Expense save(Expense expense) {

        // CREATE if not present
        if(expense.getId() == null) {
            Long id = idGenerator.getAndIncrement();
            expense.setId(id);
        }

        // Handles Insert + Update
        expenses.put(expense.getId(), expense);
        return expense;
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenses.values());
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return Optional.ofNullable(expenses.get(id));
    }

    @Override
    public List<Expense> filterByCategory(Category category) {
        // Get expenses and filter them by category and return them by converting it to list
        return expenses.values()
                .stream()
                .filter(expense -> expense.getCategory() == category)
                .toList();
    }

    @Override
    public boolean deleteById(Long id) {
        return expenses.remove(id) != null;
    }

    /**
     * Clears the in-memory repository and resets the ID generator.
     * Intended for use by integration tests to ensure test isolation.
     */
    @Override
    public void clear() {
        expenses.clear();
        idGenerator.set(1);
    }

}
