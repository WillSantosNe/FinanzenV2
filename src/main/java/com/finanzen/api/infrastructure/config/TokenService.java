package com.finanzen.api.infrastructure.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Service responsible for generating and validating JSON Web Tokens (JWT).
 * <p>
 * This infrastructure component relies on the Auth0 JWT library to handle 
 * the cryptographic signing and verification of authentication tokens.
 * It uses HMAC256 encryption and enforces short-lived tokens for security.
 * </p>
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Generates a signed JWT for an authenticated user.
     *
     * @param user the authenticated user details.
     * @return a signed JWT string valid for a short period.
     */
    public String generateToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("finanzen-api") // Quem emitiu o token
                    .withSubject(user.getUsername()) // De quem é o token 
                    .withExpiresAt(genExpirationDate()) // Quando expira o token
                    .sign(algorithm); // Assina o documento
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error generating JWT token.", ex);
        }
    }

    /**
     * Validates a given JWT and extracts the subject (username/email).
     *
     * @param token the JWT string to validate.
     * @return the subject if valid, or an empty string if invalid or expired.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("finanzen-api")
                    .build()
                    .verify(token) // Verifica se o token é valido e não expirou
                    .getSubject(); // Devolve o username que estava guardado dentro dele
            
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    /**
     * Calculates the expiration date for the token.
     *
     * @return an {@link Instant} representing the expiration time.
     */
    private Instant genExpirationDate() {
        // Token vai durar 15 minutos
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
    }
}
