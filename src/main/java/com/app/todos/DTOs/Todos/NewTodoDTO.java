package com.app.todos.DTOs.Todos;

import com.app.todos.Enums.Todos.Priority;

import java.math.BigInteger;
import java.time.LocalDate;

public record NewTodoDTO(BigInteger user_id, String title, String client, String description, String link, LocalDate due, Priority priority) {
}



