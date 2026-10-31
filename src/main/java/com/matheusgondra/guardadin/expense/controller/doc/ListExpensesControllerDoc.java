package com.matheusgondra.guardadin.expense.controller.doc;

import com.matheusgondra.guardadin.doc.annotation.ApiServerErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "List expenses", description = "List including the expenses page")
@ApiResponse(responseCode = "200", description = "Paged list of expenses successfully retrieved")
@ApiServerErrorResponse
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListExpensesControllerDoc {}
