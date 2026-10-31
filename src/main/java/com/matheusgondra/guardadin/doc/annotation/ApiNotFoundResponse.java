package com.matheusgondra.guardadin.doc.annotation;

import com.matheusgondra.guardadin.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponse(
        responseCode = "404",
        description = "Resource not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ApiNotFoundResponse {}
