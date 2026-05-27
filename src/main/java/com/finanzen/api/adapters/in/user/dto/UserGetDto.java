package com.finanzen.api.adapters.in.user.dto;

import com.finanzen.api.domain.user.Role;

public record UserGetDto(
        Long id,
        String email,
        Role role
) {
}
