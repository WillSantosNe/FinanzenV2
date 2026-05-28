package com.finanzen.api.adapters.in.user.dto;

import com.finanzen.api.domain.user.Role;

/**
 * Data Transfer Object for exposing user profile information.
 * <p>
 * Excludes sensitive data (like password) to ensure API security.
 * </p>
 */
public record UserGetDto(
        Long id,
        String email,
        Role role
) {
}