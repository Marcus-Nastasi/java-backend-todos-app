package com.app.todos.domain.Users.DTOs;

public record UpdateDTO(
        String name,
        String email,
        String currentPassword,
        String newPassword
) {}
