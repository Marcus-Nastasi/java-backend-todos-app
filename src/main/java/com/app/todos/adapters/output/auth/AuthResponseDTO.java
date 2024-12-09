package com.app.todos.adapters.output.auth;


import com.app.todos.domain.users.User;

public record AuthResponseDTO(
        String token,
        User user
) {}
