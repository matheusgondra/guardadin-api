package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.category.entity.CategoryType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AddExpenseParam(UUID userId, String title, BigDecimal amount, LocalDate date, CategoryType category) {}
