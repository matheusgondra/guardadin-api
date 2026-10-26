package com.matheusgondra.guardadin.auth.service;

import com.matheusgondra.guardadin.auth.exception.InvalidCredentialsException;
import com.matheusgondra.guardadin.auth.usecase.LoginParam;
import com.matheusgondra.guardadin.cryptography.service.TokenService;
import com.matheusgondra.guardadin.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @InjectMocks
    private LoginService sut;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private User user;

    @Mock
    private Authentication authMock;

    private LoginParam loginParam;

    @BeforeEach
    void setUp() {
        loginParam = new LoginParam("any@email.com", "anyPassword");

        lenient().when(authMock.getPrincipal()).thenReturn(user);
        lenient().when(user.getId()).thenReturn(UUID.randomUUID());
        lenient()
                .when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        lenient().when(tokenService.generateToken(anyString())).thenReturn("anyToken");
    }

    @Test
    void shouldCallAuthenticationManager() {
        sut.execute(loginParam);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldCallTokenService() {

        sut.execute(loginParam);

        verify(tokenService).generateToken(user.getId().toString());
    }

    @Test
    void shouldReturnToken() {
        var response = sut.execute(loginParam);

        assertEquals("anyToken", response);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenAuthenticationFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(InvalidCredentialsException.class, () -> sut.execute(loginParam));
    }
}