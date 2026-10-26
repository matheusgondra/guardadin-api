package com.matheusgondra.guardadin.expense.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.matheusgondra.guardadin.BaseIntegrationTest;
import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import com.matheusgondra.guardadin.category.repository.CategoryRepository;
import com.matheusgondra.guardadin.cryptography.service.TokenService;
import com.matheusgondra.guardadin.expense.entity.Expense;
import com.matheusgondra.guardadin.expense.respository.ExpenseRepository;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class ListExpensesControllerTest extends BaseIntegrationTest {
    private final String path = "/api/v1/expense";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = User.builder()
                .firstName("User1")
                .lastName("Test")
                .email("user1@email.com")
                .password(passwordEncoder.encode("senha123"))
                .build();
        User user2 = User.builder()
                .firstName("User2")
                .lastName("Test")
                .email("user2@email.com")
                .password(passwordEncoder.encode("senha123"))
                .build();

        userRepository.saveAll(List.of(user1, user2));

        token = tokenService.generateToken(user1.getId().toString());

        Category foodCategory = categoryRepository.findByName(CategoryType.FOOD).orElseThrow();
        Category educationCategory =
                categoryRepository.findByName(CategoryType.EDUCATION).orElseThrow();

        for (int i = 1; i <= 3; i++) {
            Expense expense = Expense.builder()
                    .title("Despesa User 1 - Item " + i)
                    .amount(new BigDecimal("50.00"))
                    .date(LocalDate.now())
                    .user(user1)
                    .category(foodCategory)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            expenseRepository.save(expense);
        }

        Expense expUser2 = Expense.builder()
                .title("Despesa User 2")
                .amount(new BigDecimal("100.00"))
                .date(LocalDate.now())
                .user(user2)
                .category(educationCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        expenseRepository.save(expUser2);
    }

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnPagedExpensesOnlyForLoggedUser() {
        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
