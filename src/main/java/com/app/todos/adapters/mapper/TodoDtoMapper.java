package com.app.todos.adapters.mapper;

import com.app.todos.adapters.input.todos.TodosRequestDTO;
import com.app.todos.adapters.input.todos.TodosUpdateDTO;
import com.app.todos.adapters.output.todos.TodoResponseDto;
import com.app.todos.domain.todos.Todo;

public class TodoDtoMapper {
    public TodoResponseDto mapToResponse(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getUser_id(),
                todo.getClient(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getLink(),
                todo.getCreation(),
                todo.getDue(),
                todo.getStatus(),
                todo.getPriority(),
                todo.getLast_updated()
        );
    }

    public Todo mapFromRequest(TodosRequestDTO todo) {
        return new Todo(
                null,
                todo.user_id(),
                todo.client(),
                todo.title(),
                todo.description(),
                todo.link(),
                null,
                todo.due(),
                null,
                todo.priority(),
                null
        );
    }

    public Todo mapFromUpdateDto(TodosUpdateDTO todo) {
        return new Todo(
                null,
                null,
                todo.client(),
                todo.title(),
                todo.description(),
                todo.link(),
                null,
                todo.due(),
                null,
                todo.priority(),
                null
        );
    }
}
