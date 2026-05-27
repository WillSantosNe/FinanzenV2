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
     * Handles the creation of a new user securely tied to the logged-in user.
     */
    @PostMapping("/register")
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserCreateDto dto) {

        User userFromDto = new User(
                null,
                dto.email(),
                dto.password(),
                null
        );

        User user = createUserPort.create(userFromDto);
        UserGetDto userGetDto = new UserGetDto(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userGetDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto dto) {

        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.email(), dto.password());

        return ResponseEntity.ok(authenticateUserPort.authenticate(loginRequestDto));
    }
}
