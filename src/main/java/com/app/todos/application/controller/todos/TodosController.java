package com.app.todos.application.controller.todos;

import com.app.todos.domain.todos.DTOs.TodosPageResponseDto;
import com.app.todos.domain.todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.todos.DTOs.TodosUpdateDTO;
import com.app.todos.domain.todos.Todo;
import com.app.todos.application.service.todos.TodosService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/todos")
@SecurityRequirement(name = "Bearer Authentication")
public class TodosController {

    @Autowired
    private TodosService todosService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Todo> getSingle(
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        Todo todo = todosService.get(id, token);
        return ResponseEntity.ok(todo);
    }

    @GetMapping(value = "/all/{user_id}")
    public ResponseEntity<TodosPageResponseDto> all(
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size,
            @RequestParam("query") @Nullable String query,
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        if (size <= 0) size = 10;
        String token = headers.get("authorization").replace("Bearer ", "");
        TodosPageResponseDto todoList = todosService.getAll(user_id, token, query, page, size);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping(value = "/done/{user_id}")
    public ResponseEntity<TodosPageResponseDto> getDone(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers,
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size
    ) {
        if (size <= 0) size = 10;
        String token = headers.get("authorization").replace("Bearer ", "");
        TodosPageResponseDto todoList = todosService.getDone(user_id, token, page, size);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping(value = "/progress/{user_id}")
    public ResponseEntity<TodosPageResponseDto> getProgress(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers,
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size
    ) {
        if (size <= 0) size = 10;
        String token = headers.get("authorization").replace("Bearer ", "");
        TodosPageResponseDto todoList = todosService.getProgress(user_id, token, page, size);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping(value = "/pending/{user_id}")
    public ResponseEntity<TodosPageResponseDto> getPending(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers,
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size
    ) {
        if (size <= 0) size = 10;
        String token = headers.get("authorization").replace("Bearer ", "");
        TodosPageResponseDto todoList = todosService.getPending(user_id, token, page, size);
        return ResponseEntity.ok(todoList);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Todo> newTodo(
            @RequestBody @Validated TodosRequestDTO data
    ) {
        Todo todo = todosService.newTodo(data);
        return ResponseEntity
            .created(URI.create("/api/todos/get/" + todo.getId()))
            .body(todo);
    }

    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<Todo> update(
            @PathVariable BigInteger id,
            @RequestBody @Validated TodosUpdateDTO data,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        Todo todo = todosService.update(id, data, token);
        return ResponseEntity.ok(todo);
    }

    @PatchMapping(value = "/update/status/{id}")
    public ResponseEntity<Todo> updateStatus(
            @PathVariable BigInteger id,
            @RequestBody TodosStatusDTO data,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        Todo todo = todosService.updateStatus(id, data, token);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Todo> delete(
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        Todo todo = todosService.delete(id, token);
        return ResponseEntity.ok(todo);
    }
}
