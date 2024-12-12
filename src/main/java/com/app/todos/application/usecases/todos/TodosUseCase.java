package com.app.todos.application.usecases.todos;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.domain.todos.Status;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.todos.TodosPage;

import java.math.BigInteger;
import java.time.LocalDate;

public class TodosUseCase {
    private final TodosGateway todosGateway;
    private final UserUseCase userUseCase;

    public TodosUseCase(TodosGateway todosRepoGateway, UserUseCase userUseCase) {
        this.todosGateway = todosRepoGateway;
        this.userUseCase = userUseCase;
    }

    public TodosPage getAll(
            BigInteger user_id,
            String token,
            String query,
            String client,
            // status: 0 = pending, 1 = in progress, 2 = done
            String status,
            // priority: 0 = low, 1 = medium, 2 = high
            String priority,
            LocalDate from,
            LocalDate to,
            LocalDate due,
            int page,
            int size
    ) {
        userUseCase.validateUserToken(user_id, token);
        return todosGateway.getAll(
            user_id,
            query,
            client,
            status != null && !status.isEmpty() ? Integer.parseInt(status) : null,
            priority != null && !priority.isEmpty() ? Integer.parseInt(priority) : null,
            from != null ? from : LocalDate.of(1000, 1, 1),
            to != null ? to : LocalDate.of(2500, 1, 1),
            due != null ? due : LocalDate.of(2500, 1, 1),
            page,
            size
        );
    }

    public Todo get(BigInteger id, String token) {
        Todo t = todosGateway.get(id);
        userUseCase.validateUserToken(t.getUser_id(), token);
        return t;
    }

    public Todo newTodo(Todo data) {
        data.setStatus(Status.PENDING);
        data.setCreation(LocalDate.now());
        data.setLast_updated(LocalDate.now());
        return todosGateway.create(data);
    }

    public Todo update(BigInteger id, Todo data, String token) {
        Todo t = todosGateway.get(id);
        userUseCase.validateUserToken(t.getUser_id(), token);
        t.setLast_updated(LocalDate.now());
        return todosGateway.update(id, t.updateDetails(data));
    }

    public Todo updateStatus(BigInteger id, Status data, String token) {
        Todo t = todosGateway.get(id);
        userUseCase.validateUserToken(t.getUser_id(), token);
        t.setStatus(data);
        t.setLast_updated(LocalDate.now());
        return todosGateway.update(id, t);
    }

    public Todo delete(BigInteger id, String token) {
        Todo t = todosGateway.get(id);
        userUseCase.validateUserToken(t.getUser_id(), token);
        return todosGateway.delete(id);
    }
}
