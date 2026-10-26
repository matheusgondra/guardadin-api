package com.matheusgondra.guardadin.exception.dto;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, int statusCode, OffsetDateTime timestamp) {
    public ErrorResponse(String message, HttpStatus status) {
        this(message, status.value(), OffsetDateTime.now());
    }

    public static ErrorResponse conflict(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    public static ErrorResponse unauthorized(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public static ErrorResponse serverError() {
        return new ErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
