package com.app.todos.DTOs.Todos;

import java.math.BigInteger;
import java.time.LocalDate;

public record NewTodoDTO(BigInteger user_id, String client, String description, String link, LocalDate due) {
}



