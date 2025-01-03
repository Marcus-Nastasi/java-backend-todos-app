package com.app.todos.application.gateway.todos;

import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.todos.TodosPage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

public interface TodosGateway {
    TodosPage getAll(
        BigInteger user_id,
        String query,
        String client,
        Integer status,
        Integer priority,
        LocalDate from,
        LocalDate to,
        LocalDate due,
        int page,
        int size
    );
    Todo get(BigInteger id);
    Todo create(Todo todo);
    Todo update(BigInteger id, Todo todo);
    Todo delete(BigInteger id);
    Map<String, BigDecimal> getMetrics(BigInteger user_id, String client, LocalDate from, LocalDate to);
}
