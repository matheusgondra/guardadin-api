package com.matheusgondra.guardadin.exception.dto;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, int statusCode, OffsetDateTime timestamp) {
    public ErrorResponse(String message, HttpStatus status) {
        this(message, status.value(), OffsetDateTime.now());
    }

    public static ErrorResponse conflict(RuntimeException ex) {
        return build(ex, HttpStatus.CONFLICT);
    }

    public static ErrorResponse unauthorized(RuntimeException ex) {
        return build(ex, HttpStatus.UNAUTHORIZED);
    }

    public static ErrorResponse notFound(RuntimeException ex) {
        return build(ex, HttpStatus.NOT_FOUND);
    }

    public static ErrorResponse serverError() {
        return new ErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorResponse build(RuntimeException ex, HttpStatus status) {
        return new ErrorResponse(ex.getMessage(), status);
    }
}
