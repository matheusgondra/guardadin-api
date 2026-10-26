package com.matheusgondra.guardadin.cryptography.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashServiceTest {
    private final String RAW_PASSWORD = "secretPassword123";
    private final String HASHED_PASSWORD = "hashedPasswordXYZ";

    @InjectMocks
    private HashService sut;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCallEncodeOnPasswordEncoder() {
        sut.hash(RAW_PASSWORD);

        verify(passwordEncoder, times(1)).encode(RAW_PASSWORD);
    }

    @Test
    void shouldReturnHashedString() {
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);

        String result = sut.hash(RAW_PASSWORD);

        assertEquals(HASHED_PASSWORD, result);
    }
}
