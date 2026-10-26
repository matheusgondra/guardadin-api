package com.matheusgondra.guardadin.exception.dto;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ValidationErrorResponse(
        String message, int status, OffsetDateTime timestamp, List<FieldErrorDetails> errors) {
    public record FieldErrorDetails(String field, String error) {}

    public ValidationErrorResponse(String message, HttpStatus status, List<FieldErrorDetails> errors) {
        this(message, status.value(), OffsetDateTime.now(), errors);
    }
}
