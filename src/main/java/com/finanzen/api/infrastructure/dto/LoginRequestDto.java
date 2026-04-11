package com.finanzen.api.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for user login requests.
 * <p>
 * This record encapsulates the user's credentials. It uses Jakarta Validation 
 * to perform fail-fast checks (ensuring the email format is valid and fields 
 * are not blank) before the request is processed by the authentication layer.
 * </p>
 */
public record LoginRequestDto(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {}
