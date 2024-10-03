package com.app.todos.domain.Todos.DTOs;

import com.app.todos.resources.enums.todos.Priority;

import java.math.BigInteger;
import java.time.LocalDate;

public record NewTodoDTO(BigInteger user_id, String title, String client, String description, String link, LocalDate due, Priority priority) {
}



