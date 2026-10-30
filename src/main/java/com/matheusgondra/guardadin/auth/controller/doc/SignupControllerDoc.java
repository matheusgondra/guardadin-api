package com.matheusgondra.guardadin.auth.controller.doc;

import com.matheusgondra.guardadin.doc.annotation.ApiBadRequestResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiConflictResponse;
import com.matheusgondra.guardadin.doc.annotation.ApiServerErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Signup", description = "Register a new user in application")
@ApiResponse(responseCode = "201", description = "Register a new user")
@ApiConflictResponse
@ApiBadRequestResponse
@ApiServerErrorResponse
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignupControllerDoc {}
