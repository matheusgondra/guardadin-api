package com.matheusgondra.guardadin.expense.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.exception.ExpenseNotFoundException;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseParam;
import com.matheusgondra.guardadin.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateExpenseServiceTest {
    private final UUID id = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final User user = User.builder().id(userId).build();
    private final UpdateExpenseParam param =
            new UpdateExpenseParam(id, user, "title test", null, null, CategoryType.TRANSPORT);

    private Expense expense;

    @InjectMocks
    private UpdateExpenseService sut;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = Category.builder().name(CategoryType.FOOD).build();
        expense = Expense.builder().id(id).user(user).category(category).build();

        lenient()
                .when(expenseRepository.findByIdAndUser(param.id(), param.user()))
                .thenReturn(Optional.of(expense));

        lenient()
                .when(categoryRepository.findByName(CategoryType.TRANSPORT))
                .thenReturn(Optional.of(
                        Category.builder().name(CategoryType.TRANSPORT).build()));
    }

    @Test
    void shouldCallFindByIdAndUserOnExpenseRepository() {
        sut.execute(param);

        verify(expenseRepository).findByIdAndUser(param.id(), param.user());
    }

    @Test
    void shouldThrowWhenFindByIdAndUserReturnEmpty() {
        when(expenseRepository.findByIdAndUser(param.id(), param.user())).thenReturn(Optional.empty());

        assertThrows(ExpenseNotFoundException.class, () -> sut.execute(param));
    }

    @Test
    void shouldUpdateExpenseWithNoNullValues() {
        sut.execute(param);

        verify(categoryRepository).findByName(CategoryType.TRANSPORT);
        assertEquals(expense.getTitle(), param.title());
        assertEquals(expense.getCategory().getName(), param.category());
    }

    @Test
    void shouldCallSaveOnExpenseRepository() {
        sut.execute(param);

        verify(expenseRepository).save(expense);
    }
}
