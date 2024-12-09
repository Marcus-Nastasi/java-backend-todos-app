package com.app.todos.infrastructure.gateway.auth;

import com.app.todos.application.exception.AppException;
import com.app.todos.application.gateway.auth.TokenGateway;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TokenProvider implements TokenGateway {

    @Value("${spring.security.token.secret}")
    private String secret;

    public String generate(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT
                .create()
                .withIssuer("todos-api")
                .withSubject(email)
                .withExpiresAt(exp())
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new AppException(e.getMessage());
        }
    }

    public String validate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT
                .require(algorithm)
                .withIssuer("todos-api")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant exp() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}
