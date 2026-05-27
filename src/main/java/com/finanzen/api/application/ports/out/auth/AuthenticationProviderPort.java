package com.finanzen.api.application.ports.out.auth;

public interface AuthenticationProviderPort {
    void authenticate(String email, String password);
}
