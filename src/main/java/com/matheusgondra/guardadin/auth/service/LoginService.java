package com.matheusgondra.guardadin.auth.service;

import com.matheusgondra.guardadin.auth.exception.InvalidCredentialsException;
import com.matheusgondra.guardadin.auth.usecase.LoginParam;
import com.matheusgondra.guardadin.auth.usecase.LoginUseCase;
import com.matheusgondra.guardadin.cryptography.service.TokenService;
import com.matheusgondra.guardadin.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public String execute(LoginParam param) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(param.email(), param.password());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            User user = (User) authentication.getPrincipal();
            if (user == null) {
                throw new InvalidCredentialsException();
            }
            String accessToken = tokenService.generateToken(user.getId().toString());

            return accessToken;
        } catch (Exception ex) {
            throw new InvalidCredentialsException();
        }
    }
}
