package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ListExpensesUseCase {
    Page<ExpenseDetails> execute(UUID userId, Pageable pageable);
}
