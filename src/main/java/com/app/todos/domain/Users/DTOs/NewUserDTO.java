package com.app.todos.domain.Users.DTOs;

public record NewUserDTO(
        String name,
        String email,
        String password
) {}
