package com.app.todos.application.gateway.auth;

public interface PasswordGateway {
    String encode(String raw_password);
    boolean matches(String raw_password, String password);
}
