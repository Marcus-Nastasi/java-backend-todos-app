package com.app.todos.adapters.output.todos;

import com.app.todos.infrastructure.entity.todos.TodoEntity;

import java.io.Serializable;
import java.util.List;

public record TodosPageResponseDto(
        int page,
        int per_page,
        int total,
        List<TodoEntity> data
) implements Serializable {}
