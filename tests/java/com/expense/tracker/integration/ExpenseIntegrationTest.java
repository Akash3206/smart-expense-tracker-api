package com.expense.tracker.integration;

import com.expense.tracker.model.Category;
import com.expense.tracker.repository.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        expenseRepository.clear();
    }

    // Helper
    private void createExpense(
            String title,
            BigDecimal amount,
            Category category,
            LocalDate date) throws Exception {

        String json = """
            {
                "title": "%s",
                "amount": %s,
                "category": "%s",
                "date": "%s"
            }
            """.formatted(
                title,
                amount,
                category,
                date
        );

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateExpenseAndRetrieveIt() throws Exception {

        // Create an expense
        createExpense(
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        // Retreive the expense
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Groceries"))
                .andExpect(jsonPath("$[0].amount").value(1250.50))
                .andExpect(jsonPath("$[0].category").value("FOOD"))
                .andExpect(jsonPath("$[0].date").value("2026-07-31"));
    }

    @Test
    void shouldCalculateTotalExpenses() throws Exception {

        // Create multiple Expenses
        createExpense(
                "Groceries",
                new BigDecimal("1000.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        createExpense(
                "Movie",
                new BigDecimal("350.25"),
                Category.ENTERTAINMENT,
                LocalDate.of(2026, 7, 31)
        );

        createExpense(
                "Fuel",
                new BigDecimal("149.25"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        // Calculate the total cost
        mockMvc.perform(get("/api/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.00"));
    }

    @Test
    void shouldDeleteExpenseSuccessfully() throws Exception {

        createExpense(
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        // Delete the expense
        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isNoContent());

        // Verify repository is empty
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingExpense() throws Exception {

        mockMvc.perform(delete("/api/expenses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Expense not found with id: 999"));
    }
}