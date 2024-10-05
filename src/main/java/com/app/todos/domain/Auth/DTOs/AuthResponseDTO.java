package com.app.todos.domain.Auth.DTOs;

import org.springframework.security.core.userdetails.UserDetails;

public record AuthResponseDTO(
        String token,
        UserDetails user
) {}
