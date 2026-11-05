package com.matheusgondra.guardadin.expense.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import com.matheusgondra.guardadin.expense.dto.UpdateExpenseRequestDTO;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

class UpdateExpenseControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/expense/{id}";
    private final UpdateExpenseRequestDTO dto = new UpdateExpenseRequestDTO("other title", null, null, null);

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Expense expense;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();

        Category otherCategory =
                categoryRepository.findByName(CategoryType.OTHERS).orElseThrow();

        expense = Expense.builder()
                .title("test title")
                .amount(new BigDecimal("500.2"))
                .date(LocalDate.now())
                .user(loggedUser)
                .category(otherCategory)
                .build();

        expenseRepository.save(expense);
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
    }

    @Test
    void shouldReturn200OnSuccess() {
        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put(path, expense.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(expense.getId().toString()))
                .body("title", equalTo(dto.title()));
    }

    @Test
    void shouldReturn404WhenExpenseNotFound() {
        ErrorResponse errorResponse = given().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put(path, UUID.randomUUID().toString())
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertThat(errorResponse.statusCode()).isEqualTo(404);
        assertThat(errorResponse.message()).isEqualTo("Expense not found");
        assertThat(errorResponse.timestamp()).isNotNull();
    }
}
