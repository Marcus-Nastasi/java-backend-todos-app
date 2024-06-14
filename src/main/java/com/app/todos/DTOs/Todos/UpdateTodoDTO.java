package com.app.todos.DTOs.Todos;

import java.time.LocalDate;

public record UpdateTodoDTO(String client, String description, String link, LocalDate due) {
}




