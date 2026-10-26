package com.matheusgondra.guardadin.exception.controller;

import com.matheusgondra.guardadin.auth.exception.InvalidCredentialsException;
import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import com.matheusgondra.guardadin.exception.dto.ValidationErrorResponse;
import com.matheusgondra.guardadin.user.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.conflict(ex);

        log.error("Conflict: {}", errorResponse);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.unauthorized(ex);

        log.error("unauthorized: {}", errorResponse);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ValidationErrorResponse.FieldErrorDetails> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorResponse.FieldErrorDetails(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        var error = new ValidationErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                fieldErrors
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", HttpStatus.UNAUTHORIZED);

        log.error("Authentication Error: {}", errorResponse, ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.serverError();

        log.error("Exception: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
