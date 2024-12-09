package com.app.todos.adapters.input.users;

public record UserRequestDTO(
        String name,
        String email,
        String password
) {}
