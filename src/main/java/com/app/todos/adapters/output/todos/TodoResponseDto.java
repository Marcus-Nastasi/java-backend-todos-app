package com.app.todos.adapters.output.todos;

import com.app.todos.domain.todos.Priority;
import com.app.todos.domain.todos.Status;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

public record TodoResponseDto(
        BigInteger id,
        BigInteger user_id,
        String client,
        String title,
        String description,
        String link,
        LocalDate creation,
        LocalDate due,
        Status status,
        Priority priority,
        LocalDate last_updated
) implements Serializable {}
