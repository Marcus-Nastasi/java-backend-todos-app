package com.app.todos.infrastructure.gateway.todos;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.todos.TodosPage;
import com.app.todos.infrastructure.entity.todos.TodoEntity;
import com.app.todos.infrastructure.mapper.todo.TodoEntityMapper;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;
import java.time.LocalDate;

public class TodosRepoGateway implements TodosGateway {

    private final TodosRepo todosRepo;
    private final TodoEntityMapper todoEntityMapper;

    public TodosRepoGateway(TodosRepo todosRepo, TodoEntityMapper todoEntityMapper) {
        this.todosRepo = todosRepo;
        this.todoEntityMapper = todoEntityMapper;
    }

    @Override
    public TodosPage getAll(BigInteger user_id, String query, String client, Integer status, Integer priority, LocalDate from, LocalDate to, LocalDate due, int page, int size) {
        Page<TodoEntity> todoEntities = todosRepo.getUserTodos(
            user_id,
            query,
            client,
            status,
            priority,
            from,
            to,
            due,
            PageRequest.of(page, size)
        );
        return new TodosPage(
            todoEntities.getNumber(),
            todoEntities.getSize(),
            todoEntities.getTotalPages(),
            todoEntities.map(todoEntityMapper::mapFromTodoEntity).toList()
        );
    }

    @Override
    public Todo get(BigInteger id) {
        return todoEntityMapper.mapFromTodoEntity(todosRepo.findById(id).orElseThrow());
    }

    @Override
    public Todo create(Todo todo) {
        return todoEntityMapper.mapFromTodoEntity(todosRepo.save(todoEntityMapper.mapToTodoEntity(todo)));
    }

    @Override
    public Todo update(BigInteger id, Todo todo) {
        return todoEntityMapper.mapFromTodoEntity(todosRepo.save(todoEntityMapper.mapToTodoEntity(todo)));
    }

    @Override
    public Todo delete(BigInteger id) {
        Todo todo = this.get(id);
        todosRepo.deleteById(id);
        return todo;
    }
}
