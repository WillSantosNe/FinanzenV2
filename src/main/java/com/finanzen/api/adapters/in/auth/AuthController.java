package com.finanzen.api.adapters.in.auth;

import com.finanzen.api.adapters.in.auth.dto.UserLoginDto;
import com.finanzen.api.adapters.in.user.dto.UserCreateDto;
import com.finanzen.api.adapters.in.user.dto.UserGetDto;
import com.finanzen.api.application.dto.auth.LoginRequestDto;
import com.finanzen.api.application.ports.in.auth.AuthenticateUserPort;
import com.finanzen.api.application.ports.in.user.CreateUserPort;
import com.finanzen.api.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller acting as the primary inbound adapter for authentication endpoints.
 * <p>
 * This adapter is responsible for handling HTTP requests related to user access,
 * including registration and login. It enforces input validation using Jakarta
 * Validation and maps inbound DTOs to the application's internal contract
 * (Inbound Ports), ensuring that the web layer remains decoupled from business logic.
 * </p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserPort authenticateUserPort;
    private final CreateUserPort createUserPort;

    public AuthController(AuthenticateUserPort authenticateUserPort, CreateUserPort createUserPort) {
        this.authenticateUserPort = authenticateUserPort;
        this.createUserPort = createUserPort;
    }

    /**
     * Handles the registration of a new user.
     *
     * @param dto the registration data transfer object.
     * @return a 201 Created response containing the basic user profile info.
     */
    @PostMapping("/register")
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserCreateDto dto) {
        // Mapeia DTO de entrada para objeto de domínio puro
        User userFromDto = new User(null, dto.email(), dto.password(), null);

        // Chama a porta de entrada (Use Case)
        User user = createUserPort.create(userFromDto);

        // Mapeia o resultado para um DTO de saída seguro
        UserGetDto userGetDto = new UserGetDto(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userGetDto);
    }

    /**
     * Authenticates a user and returns a security token.
     *
     * @param dto the login credentials.
     * @return a 200 OK response with the generated JWT string.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto dto) {
        // Traduz o DTO de entrada para o formato que a porta de entrada exige
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.email(), dto.password());

        // Chama a porta de entrada e retorna o token gerado
        return ResponseEntity.ok(authenticateUserPort.authenticate(loginRequestDto));
    }
}