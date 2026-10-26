package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.category.exception.CategoryNotFoundException;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.expense.usecase.AddExpenseParam;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.exception.UserNotFoundException;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AddExpenseServiceTest {
    private final UUID userId = UUID.randomUUID();
    private final AddExpenseParam param = new AddExpenseParam(
            userId,
            "test title",
            new BigDecimal("100.20"),
            LocalDate.now(),
            CategoryType.EDUCATION
    );
    private final User userMock = User.builder().id(userId).build();
    private final Category categoryMock = Category.builder()
            .id(1)
            .name(CategoryType.EDUCATION)
            .build();
    private final Expense expenseMock = Expense.builder()
            .id(UUID.randomUUID())
            .title(param.title())
            .amount(param.amount())
            .date(param.date())
            .user(userMock)
            .category(categoryMock)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    @InjectMocks
    private AddExpenseService sut;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;


    @BeforeEach
    void setUp() {


        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        lenient().when(categoryRepository.findByName(CategoryType.EDUCATION)).thenReturn(Optional.of(categoryMock));
        lenient().when(expenseRepository.save(any(Expense.class))).thenReturn(expenseMock);
    }

    @Test
    void shouldCallFindByIdOnUserRepository() {
        sut.execute(param);

        verify(userRepository).findById(param.userId());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sut.execute(param));
    }

    @Test
    void shouldCallFindByNameOnCategoryRepository() {
        sut.execute(param);

        verify(categoryRepository).findByName(param.category());
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        when(categoryRepository.findByName(param.category())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> sut.execute(param));
    }

    @Test
    void shouldCallSaveOnExpenseRepository() {
        sut.execute(param);

        Expense expected = Expense.builder()
                .title(param.title())
                .amount(param.amount())
                .date(param.date())
                .user(userMock)
                .category(categoryMock)
                .build();


        verify(expenseRepository).save(expected);
    }

    @Test
    void shouldReturnExpenseDetailsOnSuccess() {
        ExpenseDetails result = sut.execute(param);

        assertNotNull(result);
        assertEquals(expenseMock.getId(), result.id());
        assertEquals(param.title(), result.title());
        assertEquals(param.amount(), result.amount());
        assertEquals(param.category(), result.category());
        assertEquals(param.date(), result.date());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());
    }
}