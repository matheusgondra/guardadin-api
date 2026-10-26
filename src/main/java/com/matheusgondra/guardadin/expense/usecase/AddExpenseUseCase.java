package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;

public interface AddExpenseUseCase {
    ExpenseDetails execute(AddExpenseParam param);
}
