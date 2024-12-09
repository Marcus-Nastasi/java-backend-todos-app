package com.app.todos.application.gateway.auth;

public interface TokenGateway {
    String generate(String email);
    String validate(String token);
}
