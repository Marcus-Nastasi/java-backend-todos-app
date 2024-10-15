package com.app.todos.domain.users.DTOs;

public record UserUpdateDTO(
        String name,
        String email,
        String currentPassword,
        String newPassword
) {}
