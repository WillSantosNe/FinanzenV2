package com.finanzen.api.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for user login requests.
 * <p>
 * This record serves as the inbound data contract for the application layer.
 * It uses Jakarta Validation to perform fail-fast checks at the boundary,
 * ensuring that only structurally valid data (e.g., correct email format)
 * reaches the core Use Cases.
 * </p>
 * * @param email the user's email, must be a valid email format.
 * @param password the user's raw password, cannot be blank.
 */
public record LoginRequestDto(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password
) {}