package com.finanzen.api.application.ports.in.auth;

import com.finanzen.api.application.dto.auth.LoginRequestDto;

public interface AuthenticateUserPort {
    String authenticate(LoginRequestDto credentials);
}
