package com.matheusgondra.guardadin.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDetails(
        UUID id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
