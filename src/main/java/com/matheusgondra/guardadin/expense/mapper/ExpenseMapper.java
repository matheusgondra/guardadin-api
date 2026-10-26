package com.matheusgondra.guardadin.expense.mapper;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.entity.Expense;

public class ExpenseMapper {
    public static ExpenseDetails toDetails(Expense entity) {
        return new ExpenseDetails(
                entity.getId(),
                entity.getTitle(),
                entity.getAmount(),
                entity.getDate(),
                entity.getCategory().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
