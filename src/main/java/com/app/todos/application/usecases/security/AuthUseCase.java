package com.app.todos.application.usecases.security;

import com.app.todos.application.exception.AppException;
import com.app.todos.application.gateway.security.AuthGateway;
import com.app.todos.domain.users.User;

public class AuthUseCase {

    private final AuthGateway authGateway;

    public AuthUseCase(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    public User getByEmail(String email) {
        User user = authGateway.getByEmail(email);
        if (user == null) throw new AppException("user not found");
        return user;
    }

    public String login(String email, String password) {
        User user = authGateway.getByEmail(email);
        if (user == null) throw new AppException("user not found");
        return authGateway.login(email, password);
    }
}
