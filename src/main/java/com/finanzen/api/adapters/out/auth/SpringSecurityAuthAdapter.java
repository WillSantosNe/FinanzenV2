package com.finanzen.api.adapters.out.auth;

import com.finanzen.api.application.ports.out.auth.AuthenticationProviderPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Concrete Outbound Adapter responsible for user authentication using Spring Security.
 * <p>
 * This class implements the {@link AuthenticationProviderPort} driven port. It bridges
 * the application core and Spring Security's infrastructure, wrapping raw credentials
 * into a framework-specific {@link UsernamePasswordAuthenticationToken} to trigger
 * the formal authentication manager flow.
 * </p>
 */
@Component
public class SpringSecurityAuthAdapter implements AuthenticationProviderPort{

    private final AuthenticationManager authenticationManager;

    public SpringSecurityAuthAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Bridges the application's intent to authenticate with Spring Security's native mechanism.
     *
     * @param email    the target user's identity email.
     * @param password the plain-text password to validate.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    @Override
    public void authenticate(String email, String password) {
        //Converte o pedido do núcleo para o formato nativo do Spring Security
        var token = new UsernamePasswordAuthenticationToken(email, password);

        //Delega a validação real para o gerenciador do framework
        authenticationManager.authenticate(token);
    }
}