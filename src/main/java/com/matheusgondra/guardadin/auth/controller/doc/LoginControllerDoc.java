package com.matheusgondra.guardadin.auth.controller.doc;

import com.matheusgondra.guardadin.doc.annotation.ApiBadRequestResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiServerErrorResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiUnauthorizedResponse;
import com.matheusgondra.guardadin.doc.annotation.PublicRoute;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "Login",
        description =
                "Endpoint for user authentication. Validates user credentials and returns a JWT token if successful.")
@PublicRoute
@ApiResponse(responseCode = "200", description = "Login")
@ApiBadRequestResponse
@ApiUnauthorizedResponse
@ApiServerErrorResponse
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginControllerDoc {}
