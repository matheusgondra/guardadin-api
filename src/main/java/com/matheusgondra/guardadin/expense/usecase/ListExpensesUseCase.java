package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListExpensesUseCase {
    Page<ExpenseDetails> execute(UUID userId, Pageable pageable);
}
