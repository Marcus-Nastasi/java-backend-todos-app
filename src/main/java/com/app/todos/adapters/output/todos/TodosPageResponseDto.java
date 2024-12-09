package com.app.todos.adapters.output.todos;

import com.app.todos.domain.todos.Todo;

import java.io.Serializable;
import java.util.List;

public record TodosPageResponseDto(
        int page,
        int per_page,
        int total,
        List<Todo> data
) implements Serializable {}
