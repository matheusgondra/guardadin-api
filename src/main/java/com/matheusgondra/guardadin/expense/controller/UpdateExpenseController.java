package com.matheusgondra.guardadin.expense.controller;

import com.matheusgondra.guardadin.doc.annotation.ApiExpenseTag;
import com.matheusgondra.guardadin.expense.controller.doc.UpdateExpenseControllerDoc;
import com.matheusgondra.guardadin.expense.dto.ExpenseDetails;
import com.matheusgondra.guardadin.expense.dto.UpdateExpenseRequestDTO;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseParam;
import com.matheusgondra.guardadin.expense.usecase.UpdateExpenseUseCase;
import com.matheusgondra.guardadin.user.entity.User;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiExpenseTag
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/expense")
public class UpdateExpenseController {
    private final UpdateExpenseUseCase useCase;

    @UpdateExpenseControllerDoc
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDetails> handle(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UpdateExpenseRequestDTO dto) {
        UpdateExpenseParam param =
                new UpdateExpenseParam(id, user, dto.title(), dto.amount(), dto.date(), dto.category());
        log.debug("param: {}", param);

        ExpenseDetails result = useCase.execute(param);

        log.debug("result: {}", result);

        return ResponseEntity.ok(result);
    }
}
