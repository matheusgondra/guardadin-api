package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.user.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateExpenseParam(
        UUID id, User user, String title, BigDecimal amount, LocalDate date, CategoryType category) {}
