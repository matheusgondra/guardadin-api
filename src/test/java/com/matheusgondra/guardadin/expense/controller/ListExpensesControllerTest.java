package com.matheusgondra.guardadin.expense.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class ListExpensesControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/expense";

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();

        Category foodCategory = categoryRepository.findByName(CategoryType.FOOD).orElseThrow();

        for (int i = 1; i <= 3; i++) {
            Expense expense = Expense.builder()
                    .title("Despesa User 1 - Item " + i)
                    .amount(new BigDecimal("50.00"))
                    .date(LocalDate.now())
                    .user(loggedUser)
                    .category(foodCategory)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            expenseRepository.save(expense);
        }
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
    }

    @Test
    void shouldReturnPagedExpensesOnlyForLoggedUser() {
        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get(path + "?page=0&size=2")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(2))
                .body("page.size", equalTo(2))
                .body("page.totalElements", equalTo(3))
                .body("page.totalPages", equalTo(2))
                .body("page.number", equalTo(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() {
        given().contentType(ContentType.JSON).when().get(path).then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
