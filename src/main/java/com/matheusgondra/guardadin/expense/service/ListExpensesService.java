package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.mapper.ExpenseMapper;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.ListExpensesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ListExpensesService implements ListExpensesUseCase {
    private final ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<ExpenseDetails> execute(UUID userId, Pageable pageable) {
        return expenseRepository.findAllByUserId(userId, pageable)
                .map(ExpenseMapper::toDetails);
    }
}
