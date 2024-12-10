package com.app.todos.application.gateway.security;

public interface TokenGateway {
    String generate(String email);
    String validate(String token);
}
