package com.finanzen.api.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * <p>
 * This record acts as a fail-fast shield, ensuring that incoming JSON payloads
 * meet the basic requirements (valid email and minimum password length) before
 * reaching the application's core logic.
 * </p>
 */
public record UserCreateDto(
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 6, message = "The password must be at least 6 characters long.")
    String password
) {
}
