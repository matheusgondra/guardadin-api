package com.matheusgondra.guardadin.expense.controller;

import com.matheusgondra.guardadin.expense.dto.AddExpenseRequestDTO;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.usecase.AddExpenseParam;
import com.matheusgondra.guardadin.expense.usecase.AddExpenseUseCase;
import com.matheusgondra.guardadin.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/expense")
public class AddExpenseController {
    private final AddExpenseUseCase useCase;

    @PostMapping
    public ResponseEntity<ExpenseDetails> handle(
            @RequestBody @Valid AddExpenseRequestDTO dto, @AuthenticationPrincipal User user) {
        AddExpenseParam param =
                new AddExpenseParam(user.getId(), dto.title(), dto.amount(), dto.date(), dto.category());

        log.debug("param: {}", param);

        ExpenseDetails result = useCase.execute(param);

        log.debug("result: {}", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
