package com.app.todos.domain.auth.dtos;

public record AuthRequestDTO(
    String email,
    String password
) {}
