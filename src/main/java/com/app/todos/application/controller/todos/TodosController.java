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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/todos")
@SecurityRequirement(name = "Bearer Authentication")
public class TodosController {

    @Autowired
    private TodosService todosService;

    @GetMapping(value = "/get/{id}")
    @Cacheable("todos")
    public Todo getSingle(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return todosService.get(id, token);
    }

    @GetMapping(value = "/all/{user_id}")
    @Cacheable("todos")
    public TodosPageResponseDto all(
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size,
            @RequestParam("query") @Nullable String query,
            @RequestParam("client") @Nullable String client,
            @RequestParam("status") @Nullable String status,
            @RequestParam("priority") @Nullable String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate due,
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        if (size <= 0) size = 10;
        TodosPageResponseDto todoList = todosService.getAll(
            user_id,
            headers.get("authorization").replace("Bearer ", ""),
            query,
            client,
            status,
            priority,
            from,
            to,
            due,
            page,
            size
        );
        return todoList;
    }

    @PostMapping(value = "/register")
    @CacheEvict(value = "todos", allEntries = true)
    public ResponseEntity<Todo> newTodo(@RequestBody @Validated TodosRequestDTO data) {
        Todo todo = todosService.newTodo(data);
        return ResponseEntity
            .created(URI.create("/api/todos/get/" + todo.getId()))
            .body(todo);
    }

    @PatchMapping(value = "/update/{id}")
    @CacheEvict(value = "todos", allEntries = true)
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
    @CacheEvict(value = "todos", allEntries = true)
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
    @CacheEvict(value = "todos", allEntries = true)
    public ResponseEntity<Todo> delete(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        Todo todo = todosService.delete(id, token);
        return ResponseEntity.ok(todo);
    }
}
