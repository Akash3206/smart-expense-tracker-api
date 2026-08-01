package com.expense.tracker.dto;

import com.expense.tracker.model.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseRequest {

    @NotBlank(message = "Title is Required")
    private String title;

    @NotNull(message = "Amount is required") // It should not be null and 0
    @DecimalMin(
            value = "0.01",
            message = "Amount Must be greater than zero"
    )
    private BigDecimal amount;

    @NotNull(message = "Category is required")
    private  Category category;

    @NotNull(message = "Date is required")
    private LocalDate date;

}
