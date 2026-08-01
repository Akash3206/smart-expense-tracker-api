package com.expense.tracker.dto;

import com.expense.tracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private Category category;
    private LocalDate date;

}
