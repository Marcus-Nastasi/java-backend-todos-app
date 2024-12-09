package com.app.todos.adapters.output.auth;

import org.springframework.security.core.userdetails.UserDetails;

public record AuthResponseDTO(
        String token,
        UserDetails user
) {}
