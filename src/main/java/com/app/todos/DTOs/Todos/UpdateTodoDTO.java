package com.app.todos.DTOs.Todos;

import com.app.todos.Enums.Todos.Priority;

import java.time.LocalDate;

public record UpdateTodoDTO(String title, String client, String description, String link, LocalDate due, Priority priority) {
}




