package com.app.todos.domain.Users.DTOs;

public record UserUpdateDTO(
        String name,
        String email,
        String currentPassword,
        String newPassword
) {}
