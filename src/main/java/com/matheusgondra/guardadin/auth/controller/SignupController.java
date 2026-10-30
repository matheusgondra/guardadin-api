package com.matheusgondra.guardadin.auth.controller;

import com.matheusgondra.guardadin.auth.controller.doc.SignupControllerDoc;
import com.matheusgondra.guardadin.auth.dto.SignupRequestDTO;
import com.matheusgondra.guardadin.auth.dto.UserDetails;
import com.matheusgondra.guardadin.auth.usecase.SignupParam;
import com.matheusgondra.guardadin.auth.usecase.SignupUseCase;
import com.matheusgondra.guardadin.doc.annotation.ApiAuthTag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiAuthTag
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/signup")
public class SignupController {
    private final SignupUseCase useCase;

    @SignupControllerDoc
    @PostMapping
    public ResponseEntity<UserDetails> handle(@RequestBody @Valid SignupRequestDTO dto) {
        SignupParam param = new SignupParam(dto.firstName(), dto.lastName(), dto.email(), dto.password());

        log.debug("param: {}", param);

        UserDetails result = useCase.execute(param);

        log.debug("Usecase result: {}", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
