package com.app.todos.infrastructure.gateway.security;

import com.app.todos.application.gateway.security.PasswordGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordImGateway implements PasswordGateway {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String encode(String raw_password) {
        return passwordEncoder.encode(raw_password);
    }

    @Override
    public boolean matches(String raw_password, String password) {
        return passwordEncoder.matches(raw_password, password);
    }
}
