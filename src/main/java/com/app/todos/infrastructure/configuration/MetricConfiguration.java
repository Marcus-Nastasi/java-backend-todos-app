package com.app.todos.infrastructure.configuration;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.metrics.MetricUseCase;
import com.app.todos.application.usecases.users.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfiguration {
    @Bean
    public MetricUseCase metricUseCase(TodosGateway todosGateway, UserUseCase userUseCase) {
        return new MetricUseCase(todosGateway, userUseCase);
    }
}
