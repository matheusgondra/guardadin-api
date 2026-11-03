package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseParam;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateExpenseService implements UpdateExpenseUseCase {
    private final ExpenseRepository repository;

    @Override
    public ExpenseDetails execute(UpdateExpenseParam param) {
        repository.findByIdAndUser(param.id(), param.user());

        return null;
    }
}
