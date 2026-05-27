package com.finanzen.api.adapters.in.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDto(
        @NotBlank
        String email,

        @NotBlank
        String password) {
}
