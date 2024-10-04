package com.app.todos.domain.Todos.DTOs;

import com.app.todos.resources.enums.todos.Status;

public record TodosStatusDTO(
        Status status
) {}
