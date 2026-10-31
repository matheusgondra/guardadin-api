package com.matheusgondra.guardadin.expense.controller;

import com.matheusgondra.guardadin.doc.annotation.ApiExpenseTag;
import com.matheusgondra.guardadin.expense.controller.doc.ListExpensesControllerDoc;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.usecase.ListExpensesUseCase;
import com.matheusgondra.guardadin.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiExpenseTag
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/expense")
public class ListExpensesController {
    private final ListExpensesUseCase useCase;

    @ListExpensesControllerDoc
    @GetMapping
    public ResponseEntity<PagedModel<ExpenseDetails>> handle(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ExpenseDetails> result = useCase.execute(user.getId(), pageable);

        log.debug("Page: {}", result);

        PagedModel<ExpenseDetails> pagedModel = new PagedModel<>(result);

        return ResponseEntity.ok(pagedModel);
    }
}
