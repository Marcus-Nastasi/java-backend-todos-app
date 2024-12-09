package com.app.todos.infrastructure.configuration;

import com.app.todos.adapters.mapper.TodoDtoMapper;
import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.todos.TodosUseCase;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.infrastructure.gateway.todos.TodosRepoGateway;
import com.app.todos.infrastructure.mapper.todo.TodoEntityMapper;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodoConfiguration {
    @Bean
    public TodosUseCase todosUseCase(TodosGateway todosGateway, UserUseCase userUseCase) {
        return new TodosUseCase(todosGateway, userUseCase);
    }

    @Bean
    public TodosGateway todosGateway(TodosRepo todosRepo, TodoEntityMapper todoEntityMapper) {
        return new TodosRepoGateway(todosRepo, todoEntityMapper);
    }

    @Bean
    public TodoDtoMapper todoDtoMapper() {
        return new TodoDtoMapper();
    }

    @Bean
    public TodoEntityMapper todoEntityMapper() {
        return new TodoEntityMapper();
    }
}
