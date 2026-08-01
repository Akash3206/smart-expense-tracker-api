package com.expense.tracker.controller;

import com.expense.tracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.dto.ExpenseResponse;
import com.expense.tracker.model.Category;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Test
    void shouldCreateExpense() throws Exception {

        ExpenseResponse response = new ExpenseResponse(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        when(expenseService.addExpense(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/expenses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Lunch",
                                "amount": 250.00,
                                "category": "FOOD",
                                "date": "2026-07-31"
                            }
                            """)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(250.00))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.date").value("2026-07-31"));
    }

    @Test
    void shouldReturnBadRequestForInvalidExpense() throws Exception {

        mockMvc.perform(
                        post("/api/expenses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "",
                                "amount": -100,
                                "category": null,
                                "date": null
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.title").value("Title is Required"))
                .andExpect(jsonPath("$.validationErrors.amount").value("Amount Must be greater than zero"))
                .andExpect(jsonPath("$.validationErrors.category").value("Category is required"))
                .andExpect(jsonPath("$.validationErrors.date").value("Date is required"));
    }

    @Test
    void shouldGetAllExpenses() throws Exception {

        ExpenseResponse expense1 = new ExpenseResponse(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        ExpenseResponse expense2 = new ExpenseResponse(
                2L,
                "Bus Ticket",
                new BigDecimal("75.00"),
                Category.TRANSPORT,
                LocalDate.of(2026, 7, 31)
        );

        when(expenseService.getAllExpenses())
                .thenReturn(List.of(expense1, expense2));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Lunch"))
                .andExpect(jsonPath("$[0].category").value("FOOD"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Bus Ticket"))
                .andExpect(jsonPath("$[1].category").value("TRANSPORT"));

        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void shouldReturnEmptyListWhenNoExpensesExist() throws Exception {

        when(expenseService.getAllExpenses())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsZero() throws Exception {

        mockMvc.perform(
                        post("/api/expenses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Lunch",
                                "amount": 0,
                                "category": "FOOD",
                                "date": "2026-07-31"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.amount")
                        .value("Amount Must be greater than zero"));

        verify(expenseService, never())
                .addExpense(any());
    }

    @Test
    void shouldGetExpensesByCategory() throws Exception {

        ExpenseResponse expense1 = new ExpenseResponse(
                1L,
                "Lunch",
                new BigDecimal("250.00"),
                Category.FOOD,
                LocalDate.of(2026, 7, 31)
        );

        ExpenseResponse expense2 = new ExpenseResponse(
                2L,
                "Groceries",
                new BigDecimal("1250.50"),
                Category.FOOD,
                LocalDate.of(2026, 7, 30)
        );

        when(expenseService.getExpensesByCategory(Category.FOOD))
                .thenReturn(List.of(expense1, expense2));

        mockMvc.perform(get("/api/expenses/category/FOOD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("FOOD"))
                .andExpect(jsonPath("$[1].category").value("FOOD"));

        verify(expenseService, times(1))
                .getExpensesByCategory(Category.FOOD);
    }

    @Test
    void shouldReturnEmptyListWhenCategoryHasNoExpenses() throws Exception {

        when(expenseService.getExpensesByCategory(Category.HEALTH))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/expenses/category/HEALTH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(expenseService, times(1))
                .getExpensesByCategory(Category.HEALTH);
    }

    @Test
    void shouldReturnBadRequestForInvalidCategory() throws Exception {

        mockMvc.perform(get("/api/expenses/category/INVALID"))
                .andExpect(status().isBadRequest());

        verify(expenseService, never())
                .getExpensesByCategory(any(Category.class));
    }

    @Test
    void shouldGetTotalExpenses() throws Exception {

        when(expenseService.getTotalExpenses())
                .thenReturn(new BigDecimal("1575.50"));

        mockMvc.perform(get("/api/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("1575.50"));

        verify(expenseService, times(1))
                .getTotalExpenses();
    }

    @Test
    void shouldGetTotalExpensesByCategory() throws Exception {

        when(expenseService.getTotalExpensesByCategory(Category.FOOD))
                .thenReturn(new BigDecimal("1500.50"));

        mockMvc.perform(get("/api/expenses/total/category/FOOD"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.50"));

        verify(expenseService, times(1))
                .getTotalExpensesByCategory(Category.FOOD);
    }

    @Test
    void shouldDeleteExpense() throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/api/expenses/{id}", id))
                .andExpect(status().isNoContent());

        verify(expenseService, times(1))
                .deleteExpense(id);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingExpense() throws Exception {

        Long id = 999L;

        doThrow(new ExpenseNotFoundException(id))
                .when(expenseService)
                .deleteExpense(id);

        mockMvc.perform(delete("/api/expenses/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Expense not found with id: 999"));

        verify(expenseService, times(1))
                .deleteExpense(id);
    }

    @Test
    void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {

        mockMvc.perform(
                        post("/api/expenses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {}
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.title")
                        .value("Title is Required"))
                .andExpect(jsonPath("$.validationErrors.amount")
                        .value("Amount is required"))
                .andExpect(jsonPath("$.validationErrors.category")
                        .value("Category is required"))
                .andExpect(jsonPath("$.validationErrors.date")
                        .value("Date is required"));

        verify(expenseService, never())
                .addExpense(any());
    }

    @Test
    void shouldReturnBadRequestForMalformedJson() throws Exception {

        mockMvc.perform(
                        post("/api/expenses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Lunch",
                                "amount": 250.00,
                                "category": "FOOD",
                                "date": "2026-07-31"
                            """)   // intentionally missing closing }
                )
                .andExpect(status().isBadRequest());

        verify(expenseService, never())
                .addExpense(any());
    }

    @Test
    void shouldReturnBadRequestForInvalidCategoryWhenGettingTotal() throws Exception {

        mockMvc.perform(
                        get("/api/expenses/total/category/INVALID")
                )
                .andExpect(status().isBadRequest());

        verify(expenseService, never())
                .getTotalExpensesByCategory(any(Category.class));
    }
}