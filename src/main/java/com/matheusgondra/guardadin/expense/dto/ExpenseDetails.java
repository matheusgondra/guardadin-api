package com.matheusgondra.guardadin.expense.dto;

import com.matheusgondra.guardadin.category.entity.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseDetails(
        UUID id,
        String title,
        BigDecimal amount,
        LocalDate date,
        CategoryType category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
