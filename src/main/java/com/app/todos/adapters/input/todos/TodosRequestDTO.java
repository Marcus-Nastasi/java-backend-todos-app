package com.app.todos.adapters.input.todos;

import com.app.todos.domain.todos.Priority;

import java.math.BigInteger;
import java.time.LocalDate;

public record TodosRequestDTO(
        BigInteger user_id,
        String title,
        String client,
        String description,
        String link,
        LocalDate due,
        Priority priority
) {}
