package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.exception.CategoryNotFoundException;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.exception.ExpenseNotFoundException;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseParam;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseUseCase;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateExpenseService implements UpdateExpenseUseCase {
    private final ExpenseRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public ExpenseDetails execute(UpdateExpenseParam param) {
        Expense expense =
                repository.findByIdAndUser(param.id(), param.user()).orElseThrow(ExpenseNotFoundException::new);

        this.updateExpense(expense, param);

        return null;
    }

    private void updateExpense(Expense expense, UpdateExpenseParam param) {
        boolean shouldUpdateAmount = param.amount() != null && !Objects.equals(expense.getAmount(), param.amount());
        if (shouldUpdateAmount) {
            expense.setAmount(param.amount());
        }

        boolean shouldUpdateDate = param.date() != null && !Objects.equals(expense.getDate(), param.date());
        if (shouldUpdateDate) {
            expense.setDate(param.date());
        }

        boolean shouldUpdateTitle = param.title() != null && !Objects.equals(expense.getTitle(), param.title());
        if (shouldUpdateTitle) {
            expense.setTitle(param.title());
        }

        boolean shouldUpdateCategory = param.category() != null
                && !Objects.equals(expense.getCategory().getName(), param.category());
        if (shouldUpdateCategory) {
            Category category =
                    categoryRepository.findByName(param.category()).orElseThrow(CategoryNotFoundException::new);

            expense.setCategory(category);
        }
    }
}
