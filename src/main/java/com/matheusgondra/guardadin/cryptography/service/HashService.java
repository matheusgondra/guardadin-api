package com.matheusgondra.guardadin.cryptography.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HashService {
    private final PasswordEncoder passwordEncoder;

    public String hash(String value) {
        return passwordEncoder.encode(value);
    }
}
