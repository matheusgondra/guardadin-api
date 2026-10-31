package com.matheusgondra.guardadin.doc.annotation;

import com.matheusgondra.guardadin.exception.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponse(
        responseCode = "400",
        description = "Validation failed for the request payload (e.g., missing required fields or invalid format).",
        content =
                @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ValidationErrorResponse.class)))
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ApiBadRequestResponse {}
