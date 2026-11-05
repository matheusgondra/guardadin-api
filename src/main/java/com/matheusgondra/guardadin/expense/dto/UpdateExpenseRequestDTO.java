package com.matheusgondra.guardadin.expense.dto;

import com.matheusgondra.guardadin.category.entity.CategoryType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequestDTO(
        @Size(min = 3) String title, @Positive BigDecimal amount, LocalDate date, CategoryType category) {}
