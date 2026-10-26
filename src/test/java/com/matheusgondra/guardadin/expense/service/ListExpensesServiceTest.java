package com.matheusgondra.guardadin.expense.service;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListExpensesServiceTest {
    @InjectMocks
    private ListExpensesService sut;

    @Mock
    private ExpenseRepository expenseRepository;

    @Test
    void shouldReturnPagedExpenseDetails() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Category category = Category.builder().name(CategoryType.FOOD).build();
        User user = User.builder().id(userId).build();

        Expense expense1 = Expense.builder()
                .id(UUID.randomUUID())
                .title("test title")
                .amount(new BigDecimal("150.00"))
                .date(LocalDate.now())
                .user(user)
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Page<Expense> expensePage = new PageImpl<>(List.of(expense1), pageable, 1);

        when(expenseRepository.findAllByUserId(userId, pageable)).thenReturn(expensePage);

        Page<ExpenseDetails> result = sut.execute(userId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        ExpenseDetails details = result.getContent().getFirst();
        assertEquals(expense1.getId(), details.id());
        assertEquals("test title", details.title());
        assertEquals(CategoryType.FOOD, details.category());

        verify(expenseRepository).findAllByUserId(userId, pageable);
    }
}