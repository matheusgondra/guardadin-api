package com.matheusgondra.guardadin.expense.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.cryptography.service.TokenService;
import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import com.matheusgondra.guardadin.expense.dto.AddExpenseRequestDTO;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class AddExpenseControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/expense";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private String token;
    private User loggedUser;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        loggedUser = User.builder()
                .firstName("Fulano")
                .lastName("Silva")
                .email("fulano.silve@gmail.com")
                .password(passwordEncoder.encode("Password@123"))
                .build();
        userRepository.save(loggedUser);

        token = tokenService.generateToken(loggedUser.getId().toString());
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturn201OnSuccess() {
        var dto = new AddExpenseRequestDTO(
                "test title", new BigDecimal("100.12"), LocalDate.now(), CategoryType.EDUCATION);

        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("title", equalTo(dto.title()))
                .body("amount", equalTo(dto.amount().floatValue()))
                .body("category", equalTo(dto.category().name()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    void shouldReturn401WhenTokenIsMissingOrInvalid() {
        var dto = new AddExpenseRequestDTO(
                "Tentativa de Invasão", new BigDecimal("100.00"), LocalDate.now(), CategoryType.FOOD);

        ErrorResponse errorResponse = given().contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(errorResponse.statusCode()).isEqualTo(401);
        assertThat(errorResponse.message()).isEqualTo("Unauthorized");
        assertThat(errorResponse.timestamp()).isNotNull();
    }

    @Test
    void shouldReturn400WhenPayloadIsInvalid() {
        var invalidDto = new AddExpenseRequestDTO("", new BigDecimal("-50.00"), null, null);

        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .contentType(ContentType.JSON)
                .body(invalidDto)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Validation failed"))
                .body("errors.size()", equalTo(4));
    }
}
