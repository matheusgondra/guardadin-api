package com.matheusgondra.guardadin.expense.controller.doc;

import com.matheusgondra.guardadin.doc.annotation.ApiBadRequestResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiNotFoundResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiServerErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Add Expense", description = "Add a new expense")
@ApiResponse(responseCode = "201")
@ApiBadRequestResponse
@ApiNotFoundResponse
@ApiServerErrorResponse
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AddExpenseControllerDoc {}
