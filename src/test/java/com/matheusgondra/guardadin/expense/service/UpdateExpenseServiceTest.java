package com.matheusgondra.guardadin.expense.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private final UpdateExpenseParam param = new UpdateExpenseParam(id, user, "title test", null, null);

    @InjectMocks
    private UpdateExpenseService sut;

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        lenient()
                .when(expenseRepository.findByIdAndUser(param.id(), param.user()))
                .thenReturn(Optional.of(Expense.builder().id(id).user(user).build()));
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
}
