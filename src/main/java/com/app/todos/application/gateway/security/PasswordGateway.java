package com.app.todos.application.gateway.security;

public interface PasswordGateway {
    String encode(String raw_password);
    boolean matches(String raw_password, String password);
}
