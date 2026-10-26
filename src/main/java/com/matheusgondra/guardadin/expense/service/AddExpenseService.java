package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.exception.CategoryNotFoundException;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.AddExpenseParam;
import com.matheusgondra.guardadin.expense.usecase.AddExpenseUseCase;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.exception.UserNotFoundException;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddExpenseService implements AddExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public ExpenseDetails execute(AddExpenseParam param) {
        User user = userRepository.findById(param.userId()).orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findByName(param.category()).orElseThrow(CategoryNotFoundException::new);

        Expense expense = Expense.builder()
                .title(param.title())
                .amount(param.amount())
                .date(param.date())
                .user(user)
                .category(category)
                .build();
        Expense createdExpense = expenseRepository.save(expense);

        return new ExpenseDetails(
                createdExpense.getId(),
                createdExpense.getTitle(),
                createdExpense.getAmount(),
                createdExpense.getDate(),
                createdExpense.getCategory().getName(),
                createdExpense.getCreatedAt(),
                createdExpense.getUpdatedAt());
    }
}
