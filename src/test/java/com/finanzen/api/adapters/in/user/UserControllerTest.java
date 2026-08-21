package com.finanzen.api.adapters.in.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.user.FindAllUsersPort;
import com.finanzen.api.application.ports.in.user.FindUserByIdPort;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private FindAllUsersPort findAllUsersPort;
    @MockitoBean private FindUserByIdPort findUserByIdPort;

    @MockitoBean private TokenService tokenService;
    @MockitoBean private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "email@gmail.com", roles = {"USER"})
    public void should_return_403_forbidden_when_regular_user_attempts_access() throws Exception {

        // Act &  Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    public void should_return_200_ok_when_admin_requests_all_users() throws Exception {

        // Arrange
        List<User> mockUsers = List.of(new User(1L, "admin@gmail.com", "password", Role.ADMIN));
        PageResult<User> pageResult = new PageResult<>(mockUsers, 0, 1, 1);

        BDDMockito.given(findAllUsersPort.findAll(anyInt(),anyInt())).willReturn(pageResult);

        // Act &  Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                .andExpect(status().isOk());
    }
}
