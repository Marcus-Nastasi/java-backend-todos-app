package com.app.todos.domain.Users.DTOs;

public record UserRequestDTO(
        String name,
        String email,
        String password
) {}
