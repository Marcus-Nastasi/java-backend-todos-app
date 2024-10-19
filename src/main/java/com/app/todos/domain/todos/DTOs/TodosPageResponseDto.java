package com.app.todos.domain.todos.DTOs;

import com.app.todos.domain.todos.Todo;

import java.util.List;

public record TodosPageResponseDto(
        int page,
        int per_page,
        int total,
        List<Todo> data
) {}
