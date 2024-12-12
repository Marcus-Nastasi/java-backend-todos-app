package com.app.todos.adapters.output.auth;


import com.app.todos.domain.users.User;

import java.io.Serializable;

public record AuthResponseDTO(
        String token,
        User user
) implements Serializable {}
