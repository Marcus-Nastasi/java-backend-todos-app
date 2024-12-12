package com.app.todos.application.usecases.security;

import com.app.todos.application.gateway.security.PasswordGateway;

public class PasswordUseCase {
    private final PasswordGateway passwordGateway;

    public PasswordUseCase(PasswordGateway passwordGateway) {
        this.passwordGateway = passwordGateway;
    }

    public String encode(String raw_password) {
        return passwordGateway.encode(raw_password);
    }

    public boolean matches(String raw_password, String password) {
        return passwordGateway.matches(raw_password, password);
    }
}
