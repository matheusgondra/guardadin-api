package com.matheusgondra.guardadin.expense.dto;

import com.matheusgondra.guardadin.category.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddExpenseRequestDTO(
        @NotBlank
        String title,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        LocalDate date,

        @NotNull
        CategoryType category
) {
}
