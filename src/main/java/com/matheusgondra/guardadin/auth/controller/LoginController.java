package com.matheusgondra.guardadin.auth.controller;

import com.matheusgondra.guardadin.auth.controller.doc.LoginControllerDoc;
import com.matheusgondra.guardadin.auth.dto.LoginRequestDTO;
import com.matheusgondra.guardadin.auth.dto.LoginResponseDTO;
import com.matheusgondra.guardadin.auth.usecase.LoginParam;
import com.matheusgondra.guardadin.auth.usecase.LoginUseCase;
import com.matheusgondra.guardadin.doc.annotation.ApiAuthTag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiAuthTag
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {
    private final LoginUseCase useCase;

    @LoginControllerDoc
    @PostMapping
    public ResponseEntity<LoginResponseDTO> handle(@RequestBody @Valid LoginRequestDTO dto) {
        LoginParam param = new LoginParam(dto.email(), dto.password());

        log.debug("param: {}", param);

        String accessToken = useCase.execute(param);

        LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken);

        log.debug("response: {}", responseDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
