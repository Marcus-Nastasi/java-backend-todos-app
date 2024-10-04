package com.app.todos.domain.Auth.DTOs;

public record AuthRequestDTO(
    String email,
    String password
) {}
