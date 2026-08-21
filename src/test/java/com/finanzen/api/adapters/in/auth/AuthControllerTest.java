package com.finanzen.api.adapters.in.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.adapters.in.auth.dto.UserLoginDto;
import com.finanzen.api.adapters.in.user.dto.UserCreateDto;
import com.finanzen.api.application.dto.auth.LoginRequestDto;
import com.finanzen.api.application.ports.in.auth.AuthenticateUserPort;
import com.finanzen.api.application.ports.in.user.CreateUserPort;
import com.finanzen.api.domain.user.Role;
import com.finanzen.api.domain.user.User;
import com.finanzen.api.infrastructure.config.CustomUserDetailsService;
import com.finanzen.api.infrastructure.config.SecurityConfig;
import com.finanzen.api.infrastructure.config.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticateUserPort authenticateUserPort;

    @MockitoBean
    private CreateUserPort createUserPort;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    public void should_return_200_ok_and_token_when_login_credentials_are_valid() throws Exception {
        // Arrange
        UserLoginDto loginDto = new UserLoginDto("email@gmail.com", "password");
        String token = "token";

        String payloadJson =  objectMapper.writeValueAsString(loginDto);

        BDDMockito.given(authenticateUserPort.authenticate(any(LoginRequestDto.class))).willReturn(token);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .content(payloadJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    public void should_return_401_and_error_when_login_credentials_are_invalid() throws Exception {
        // Arrange
        UserLoginDto loginDto = new UserLoginDto("email@gmail.com", "wrongPassword");
        String payloadJson =  objectMapper.writeValueAsString(loginDto);

        BDDMockito.given(authenticateUserPort.authenticate(any(LoginRequestDto.class)))
                .willThrow(new BadCredentialsException("Invalid credentials"));


        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content(payloadJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_201_created_and_user_profile_when_registration_is_valid() throws Exception {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto("email@gmail.com", "password");
        User savedUser = new User(1L, "email@gmail.com", "password", Role.USER);

        String payloadJson =  objectMapper.writeValueAsString(userCreateDto);

        BDDMockito.given(createUserPort.create(any())).willReturn(savedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .content(payloadJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("email@gmail.com"));
    }

    @Test
    public void should_return_400_bad_request_when_auth_payload_is_invalid() throws Exception {
        // Arrange
        // Sem senha
        UserLoginDto loginDto = new UserLoginDto("email@gmail.com", "");
        String payloadJson =  objectMapper.writeValueAsString(loginDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content(payloadJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
