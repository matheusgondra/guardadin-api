package com.matheusgondra.guardadin.cryptography.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TokenService {
    private final String issuer;
    private final Algorithm algorithm;

    public TokenService(@Value("${api.jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = "guardadin-api";
    }

    public String generateToken(String payload) {
        try {
            return JWT.create()
                    .withSubject(payload)
                    .withIssuer(issuer)
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException | IllegalArgumentException ex) {
            return null;
        }
    }

    public UUID getSubjectFromToken(String token) {
        try {
            String subject = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

            return UUID.fromString(subject);
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    private Instant getExpirationDate() {
        return Instant.now().plus(1, ChronoUnit.DAYS);
    }
}
