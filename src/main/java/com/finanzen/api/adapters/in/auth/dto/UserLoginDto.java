package com.finanzen.api.adapters.in.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for login credentials.
 * <p>
 * Acts as the entry point for authentication requests from the API client.
 * </p>
 */
public record UserLoginDto(
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password) {
}