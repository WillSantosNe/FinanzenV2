package com.finanzen.api.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzen.api.application.AuthService;
import com.finanzen.api.infrastructure.config.TokenService;
import com.finanzen.api.infrastructure.dto.LoginRequestDto;
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
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

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

    
    /**
     * Authenticates a user and returns a JSON Web Token (JWT).
     * <p>
     * This endpoint orchestrates the login flow: it converts the inbound DTO into 
     * an authentication token, delegates the credential verification to the 
     * {@link AuthenticationManager}, and upon successful authentication, uses the 
     * {@link TokenService} to issue a short-lived JWT for subsequent requests.
     * </p>
     *
     * @param dto the login credentials.
     * @return a 200 OK response containing the generated JWT string.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto dto) {
        
        // Pegando o dto e deixando no padrao do authenticationManager
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        // authenticationManager fará a investigação (chamar CustomUserDetailsService e compara senhas)
        var auth = authenticationManager.authenticate(usernamePassword);

        // Se estiver certo, pega o usuário autenticado
        UserDetails user = (UserDetails)auth.getPrincipal();

        // Fabrica o token
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(token);
    }
    
}
