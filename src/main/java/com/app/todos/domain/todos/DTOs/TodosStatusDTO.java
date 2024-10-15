package com.app.todos.domain.todos.DTOs;

import com.app.todos.resources.enums.todos.Status;

public record TodosStatusDTO(
        Status status
) {}
