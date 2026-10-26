package com.matheusgondra.guardadin.auth.controller;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.auth.usecase.LoginParam;
import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class LoginControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/auth/login";
    private final String rawPassword = "Password@123";
    private final String userEmail = "fulano.silva@gmail.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User testUser = User.builder()
                .firstName("Fulano")
                .lastName("Silva")
                .email(userEmail)
                .password(passwordEncoder.encode(rawPassword))
                .build();

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturn200AndTokenOnSuccess() {
        LoginParam credentials = new LoginParam(userEmail, rawPassword);

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue());
    }

    @Test
    void shouldReturn401WhenPasswordIsIncorrect() {
        LoginParam wrongCredentials = new LoginParam(userEmail, "WrongPassword");

        ErrorResponse errorResponse = given()
                .contentType(ContentType.JSON)
                .body(wrongCredentials)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(errorResponse.statusCode()).isEqualTo(401);
        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
        assertThat(errorResponse.timestamp()).isNotNull();
    }

    @Test
    void shouldReturn401WhenEmailDoesNotExist() {
        LoginParam wrongCredentials = new LoginParam("ghost@email.com", rawPassword);

        ErrorResponse errorResponse = given()
                .contentType(ContentType.JSON)
                .body(wrongCredentials)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(errorResponse.statusCode()).isEqualTo(401);
        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
    }
}