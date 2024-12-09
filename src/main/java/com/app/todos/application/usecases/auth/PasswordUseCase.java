package com.app.todos.application.usecases.auth;

import com.app.todos.application.gateway.auth.PasswordGateway;

public class PasswordUseCase {

    private final PasswordGateway passwordGateway;

    public PasswordUseCase(PasswordGateway passwordGateway) {
        this.passwordGateway = passwordGateway;
    }

    String encode(String raw_password) {
        return passwordGateway.encode(raw_password);
    }

    boolean matches(String raw_password, String password) {
        return passwordGateway.matches(raw_password, password);
    }
}
