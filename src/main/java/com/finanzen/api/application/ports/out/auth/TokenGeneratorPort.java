package com.finanzen.api.application.ports.out.auth;

import com.finanzen.api.domain.user.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
}
