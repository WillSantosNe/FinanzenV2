package com.finanzen.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Global Security Configuration for the API.
 * <p>
 * This class configures the Spring Security filter chain to enforce a stateless
 * authentication mechanism suitable for REST APIs. It disables CSRF protection
 * (as tokens will be used instead of cookies) and defines the authorization
 * rules for incoming HTTP requests.
 * </p>
 */
@Configuration
@EnableWebSecurity // Habilita a segurança na nossa API
@EnableMethodSecurity // Permite usar o @PreAuthorize nos Controllers 
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    /**
     * Configures the main security filter chain.
     *
     * @param http the {@link HttpSecurity} builder used to configure web based
     *             security.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilitando o CSRF, pois a API será Stateless (uso de Tokens)
                .csrf(csrf -> csrf.disable())

                // Definindo a política de sessão como Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definindo regras de autorização de rotas
                .authorizeHttpRequests(authorize -> authorize
                        // Libera os endpoints de autenticaçao
                        .requestMatchers("/auth/**").permitAll()

                        // Todos os outros endpoints exigirão autenticação
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * Exposes the AuthenticationManager as a Spring Bean.
     * <p>
     * The AuthenticationManager is the core interface for Spring Security's 
     * authentication process. By exposing it here, we allow the AuthController 
     * to trigger the authentication flow manually upon receiving login requests.
     * </p>
     *
     * @param authenticationConfiguration the Spring Security configuration.
     * @return the configured {@link AuthenticationManager}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // O Spring por padrão esconde o AuthenticationManager
        // Esse método é necessário para podermos chamar no controller
        return authenticationConfiguration.getAuthenticationManager();
    }
}
