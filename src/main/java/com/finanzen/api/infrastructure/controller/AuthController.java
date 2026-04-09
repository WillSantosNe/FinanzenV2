package com.finanzen.api.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzen.api.application.AuthService;
import com.finanzen.api.infrastructure.dto.UserCreateDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller acting as the inbound adapter for authentication endpoints.
 * <p>
 * This controller handles external HTTP requests for registration and login,
 * relying on Jakarta Validation for fail-fast data integrity checks.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // @RequiredArgsConstructor só considera se tiver a palavra final declarada
    private final AuthService service;

    /**
     * Endpoint to register a new user.
     *
     * @param dto the registration payload, validated automatically via {@link Valid}.
     * @return a 200 OK response if registration is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCreateDto dto) {

        service.register(dto);
        return ResponseEntity.ok().build();
    }
}
