package com.finanzen.api.infrastructure.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Infrastructure Security Filter enforcing stateless JWT authentication.
 * <p>
 * This component extends {@link OncePerRequestFilter} to guarantee it executes
 * exactly once per incoming HTTP request. It intercepts the request, extracts the
 * Bearer token from the Authorization header, validates it, and mounts the
 * authentication context inside Spring Security's thread-local storage if valid.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Intercepts incoming HTTP requests to perform JWT validation and user authentication mapping.
     *
     * @param request     the incoming HTTP request context.
     * @param response    the outgoing HTTP response context.
     * @param filterChain the servlet chain filter to delegate to the next component.
     * @throws ServletException if a servlet-specific error occurs during processing.
     * @throws IOException      if a low-level I/O error happens while forwarding the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Tenta recuperar a String do token limpa
        var token = this.recoverToken(request);

        if (token != null) {
            // 2. Valida a assinatura do token e extrai o sujeito (email)
            var login = tokenService.validateToken(token);

            if (!login.isEmpty()) {
                // 3. Carrega os detalhes do usuário baseados no banco/infra
                UserDetails user = userDetailsService.loadUserByUsername(login);

                // 4. Cria o objeto de autenticação nativo e injeta no contexto da requisição
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Encaminha a requisição para o próximo filtro ou para o Controller alvo
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to extract and clean the Bearer token from the HTTP Authorization header.
     *
     * @param request the target HTTP request.
     * @return the raw JWT string if present and properly formatted, or null otherwise.
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}