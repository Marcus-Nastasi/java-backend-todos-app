package com.app.todos.domain.users.DTOs;

public record UserRequestDTO(
        String name,
        String email,
        String password
) {}
