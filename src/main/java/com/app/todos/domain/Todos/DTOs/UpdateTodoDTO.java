package com.app.todos.domain.Todos.DTOs;

import com.app.todos.resources.enums.todos.Priority;

import java.time.LocalDate;

public record UpdateTodoDTO(
        String title,
        String client,
        String description,
        String link,
        LocalDate due,
        Priority priority
) {}
