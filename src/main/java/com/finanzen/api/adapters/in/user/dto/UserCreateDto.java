package com.finanzen.api.adapters.in.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * Data Transfer Object for creating a new user account.
 */
public record UserCreateDto(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password) {
}