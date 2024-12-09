package com.app.todos.adapters.input.users;

public record UserUpdateDTO(
        String name,
        String email,
        String currentPassword,
        String newPassword
) {}
