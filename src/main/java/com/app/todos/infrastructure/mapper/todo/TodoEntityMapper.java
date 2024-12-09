package com.app.todos.infrastructure.mapper.todo;

import com.app.todos.domain.todos.Todo;
import com.app.todos.infrastructure.entity.todos.TodoEntity;

public class TodoEntityMapper {
    public TodoEntity mapToTodoEntity(Todo todo) {
        return new TodoEntity(
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

    public Todo mapFromTodoEntity(TodoEntity todo) {
        return new Todo(
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
}
