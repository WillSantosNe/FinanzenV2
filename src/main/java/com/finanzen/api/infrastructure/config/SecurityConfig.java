package com.finanzen.api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
public class SecurityConfig {

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
                .build();
    }
}
