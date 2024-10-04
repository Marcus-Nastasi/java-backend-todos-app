package com.app.todos.application.controller.Todos;

import com.app.todos.domain.Todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.Todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.Todos.DTOs.TodosUpdateDTO;
import com.app.todos.domain.Todos.Todo;
import com.app.todos.application.service.Todos.TodosService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/todos")
@CrossOrigin(origins = {"http://192.168.0.76:3030", "http://localhost:3030"})
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
        return ResponseEntity.ok(todosService.get(id, token));
    }

    @GetMapping(value = "/all/{user_id}")
    public ResponseEntity<List<Todo>> all(
            @RequestParam("page") @DefaultValue("0") int page,
            @RequestParam("size") @DefaultValue("10") int size,
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getAll(user_id, token, page, size));
    }

    @GetMapping(value = "/done/{user_id}")
    public ResponseEntity<List<Todo>> getDone(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getDone(user_id, token));
    }

    @GetMapping(value = "/progress/{user_id}")
    public ResponseEntity<List<Todo>> getProgress(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getProgress(user_id, token));
    }

    @GetMapping(value = "/pending/{user_id}")
    public ResponseEntity<List<Todo>> getPending(
            @PathVariable BigInteger user_id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getPending(user_id, token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> newTodo(@RequestBody @Validated TodosRequestDTO data) {
        todosService.newTodo(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable BigInteger id,
            @RequestBody @Validated TodosUpdateDTO data,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.update(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/status/{id}")
    public ResponseEntity<String> updateStatus(
            @PathVariable BigInteger id,
            @RequestBody TodosStatusDTO data,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.updateStatus(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.delete(id, token);
        return ResponseEntity.accepted().build();
    }
}
