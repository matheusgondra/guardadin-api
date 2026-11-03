package com.matheusgondra.guardadin.expense.usecase;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;

public interface UpdateExpenseUseCase {
    ExpenseDetails execute(UpdateExpenseParam param);
}
