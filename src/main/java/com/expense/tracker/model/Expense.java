package com.expense.tracker.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// These Annotations from lombok reduces boilerplate code and automatically creates the constructors(Args and NoArgs), getters and setters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private Long id;
    private String title;
    private BigDecimal amount;  // Big decimal avoids monetary precision problems
    private Category category;  // FOOD, HEALTH, TRANSPORT... etc
    private LocalDate date;
}
