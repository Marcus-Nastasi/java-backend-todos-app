package com.app.todos.application.usecases.auth;

import com.app.todos.application.exception.AppException;
import com.app.todos.application.gateway.auth.AuthGateway;
import com.app.todos.domain.users.User;

public class AuthUseCase {

    private final AuthGateway authGateway;

    public AuthUseCase(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    public User getByEmail(String email) {
        return authGateway.getByEmail(email);
    }

    public String login(String email, String password) {
        User user = authGateway.getByEmail(email);
        if (user == null) throw new AppException("user not found");
        return authGateway.login(email, password);
    }
}
