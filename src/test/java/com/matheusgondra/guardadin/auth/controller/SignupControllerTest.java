package com.matheusgondra.guardadin.auth.controller;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.auth.dto.SignupRequestDTO;
import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import com.matheusgondra.guardadin.exception.dto.ValidationErrorResponse;
import com.matheusgondra.guardadin.user.exception.UserAlreadyExistsException;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


class SignupControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/auth/signup";
    private final SignupRequestDTO dto = new SignupRequestDTO(
            "Fulano",
            "Silva",
            "fulano.silva@gmail.com",
            "Password@123"
    );

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturn201OnSuccess() {
        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post(path)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("firstName", equalTo(dto.firstName()))
                .body("lastName", equalTo(dto.lastName()))
                .body("email", equalTo(dto.email()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    void shouldReturn409WhenUserAlreadyExists() {
        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post(path)
        .then()
                .statusCode(HttpStatus.CREATED.value());

        ErrorResponse response = given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post(path)
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        ErrorResponse expectedError = ErrorResponse.conflict(new UserAlreadyExistsException());

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedError);

        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void shouldReturn400OnInvalidRequest() {
        SignupRequestDTO invalidDTO = new SignupRequestDTO("", "Doe", "", "pwd");

        ValidationErrorResponse actualError = given()
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .contentType(ContentType.JSON)
                .body(invalidDTO)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actualError.status()).isEqualTo(400);
        assertThat(actualError.message()).isEqualTo("Validation failed");
        assertThat(actualError.timestamp()).isNotNull();

        assertThat(actualError.errors())
                .hasSize(3)
                .extracting("field", "error")
                .containsExactlyInAnyOrder(
                        tuple("firstName", "must not be blank"),
                        tuple("email", "must not be blank"),
                        tuple("password", "size must be between 6 and 2147483647")
                );
    }
}