package com.app.todos.infrastructure.configuration;

import com.app.todos.application.usecases.metrics.MetricUseCase;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {
    @Bean
    public MetricUseCase metricUseCase(TodosRepo todosRepo, UserUseCase userUseCase) {
        return new MetricUseCase(todosRepo, userUseCase);
    }
}
