package com.app.todos.application.gateway.security;

import com.app.todos.domain.users.User;

public interface AuthGateway {
    User getByEmail(String email);
    String login(String email, String password);
}
