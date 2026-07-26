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

/**
 * Security filter responsible for applying Rate Limiting rules to incoming HTTP requests.
 * It uses the Token Bucket algorithm (via Bucket4j) to prevent brute-force attacks
 * and resource exhaustion by limiting the number of requests a single IP can make
 * within a specific timeframe.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Guarda um balde para cada IP que tentar fazer uma requisição
    private final Map<String, Bucket> transactionCache = new ConcurrentHashMap<>();
    private final Map<String, Bucket> loginCache = new ConcurrentHashMap<>();

    /**
     * Creates a new Token Bucket for transaction endpoints.
     * Allows 5 requests per minute.
     *
     * @return A configured Bucket instance.
     */
    private Bucket createNewTransactionBucket() {
        // Refil de 5 recargas por minuto
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));

        // Capacidade máxima do Bucket
        Bandwidth limit = Bandwidth.classic(5, refill);

        // Bucket gerado
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Creates a new Token Bucket for login endpoints.
     * Applies strict limits to prevent brute-force credential stuffing.
     * Allows 5 requests per 15 minutes.
     *
     * @return A configured strict Bucket instance.
     */
    private Bucket createNewLoginBucket() {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(15));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Intercepts the HTTP request to enforce rate limits based on the requested URI.
     * Returns a 429 Too Many Requests status if the bucket is exhausted.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The security filter chain.
     * @throws ServletException If a servlet error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Identificando Rota
        String uri = request.getRequestURI();

        // Identificando IP
        String ip = request.getRemoteAddr();

        // Blindar apenas rotas de transação ou rotas sensíveis
        if (uri.startsWith("/transactions")) {

            // Pega ou cria Bucket para IP
            Bucket bucket = transactionCache.computeIfAbsent(ip, k -> createNewTransactionBucket());

            // Tenta consumir 1 Token
            boolean hasToken = bucket.tryConsume(1);

            // Retorna TOO_MANY_REQUESTS
            if (!hasToken) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Too many requests. Try again later.");
                return;
            }
        } else if (uri.startsWith("/auth/login")) {
            Bucket bucket = loginCache.computeIfAbsent(ip, k -> createNewLoginBucket());
            boolean hasToken = bucket.tryConsume(1);
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