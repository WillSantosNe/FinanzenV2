package com.finanzen.api.adapters.out.auth;

import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuthAdapter implements AuthenticationProviderPort{

    private final AuthenticationManager authenticationManager;

    public SpringSecurityAuthAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String email, String password) {

        // Gera token
        var token = new UsernamePasswordAuthenticationToken(email, password);

        // Autentica token
        authenticationManager.authenticate(token);
    }
}
