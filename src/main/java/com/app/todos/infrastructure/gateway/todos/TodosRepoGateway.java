package com.app.todos.infrastructure.gateway.todos;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;

public class TodosRepoGateway implements TodosGateway {

    private final TodosRepo todosRepo;

    public TodosRepoGateway(TodosRepo todosRepo) {
        this.todosRepo = todosRepo;
    }


}
