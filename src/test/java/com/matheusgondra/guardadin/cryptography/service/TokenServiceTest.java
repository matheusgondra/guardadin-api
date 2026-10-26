package com.matheusgondra.guardadin.cryptography.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TokenServiceTest {
    private final UUID SUBJECT = UUID.randomUUID();
    private final String PAYLOAD = SUBJECT.toString();
    private TokenService sut;

    @BeforeEach
    void setUp() {
        final String secret = "testSecret";
        this.sut = new TokenService(secret);
    }

    @Nested
    class GenerateTokenTests {
        @Test
        void shouldGenerateTokenJWT() {
            String token = sut.generateToken(PAYLOAD);

            assertNotNull(token);
            assertEquals(3, token.split("\\.").length);
        }

        @Test
        void shouldReturnNullWhenTokenCreationFails() {
            TokenService invalidSut = new TokenService("");

            String token = invalidSut.generateToken(PAYLOAD);

            assertNull(token);
        }
    }

    @Nested
    class GetSubjectFromTokenTests {
        private String token;

        @BeforeEach
        void setUp() {
            this.token = sut.generateToken(PAYLOAD);
        }

        @Test
        void shouldReturnASubjectFromValidToken() {
            UUID subject = sut.getSubjectFromToken(token);

            assertNotNull(subject);
            assertEquals(SUBJECT, subject);
        }

        @Test
        void shouldReturnNullWhenTokenIsInvalid() {
            UUID subject = sut.getSubjectFromToken("fakeToken");

            assertNull(subject);
        }
    }
}
