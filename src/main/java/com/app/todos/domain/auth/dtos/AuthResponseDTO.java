package com.app.todos.domain.auth.dtos;

import org.springframework.security.core.userdetails.UserDetails;

public record AuthResponseDTO(
        String token,
        UserDetails user
) {}
