package com.finanzen.api.adapters.in.web.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Guarda um balde para cada IP que tentar fazer uma requisição
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Refil de 5 recargas por minuto
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));

        // Capacidade máxima do Bucket
        Bandwidth limit = Bandwidth.classic(5, refill);

        // Bucket gerado
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Queremos blindar apenas rotas de transação ou rotas sensíveis
        if (request.getRequestURI().startsWith("/transactions")) {

            // Identificando IP
            String ip = request.getRemoteAddr();

            // Pega ou cria Bucket para IP
            Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

            // Tenta consumir 1 Token
            boolean hasToken = bucket.tryConsume(1);

            // Retorna TOO_MANY_REQUESTS
            if (!hasToken) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Too many requests. Try again later.");
                return;
            }
        }

        // Se tudo estiver correto, segue normalmente
        filterChain.doFilter(request, response);
    }
}