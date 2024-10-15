package com.app.todos.domain.todos.DTOs;

import com.app.todos.resources.enums.todos.Priority;

import java.time.LocalDate;

public record TodosUpdateDTO(
        String title,
        String client,
        String description,
        String link,
        LocalDate due,
        Priority priority
) {}
