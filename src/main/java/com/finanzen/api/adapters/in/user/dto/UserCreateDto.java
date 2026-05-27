package com.finanzen.api.adapters.in.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank
        String email,

        @NotBlank
        String password) {
}
