package com.app.todos.adapters.input.todos;

import com.app.todos.domain.todos.Priority;

import java.time.LocalDate;

public record TodosUpdateDTO(
        String title,
        String client,
        String description,
        String link,
        LocalDate due,
        Priority priority
) {}
