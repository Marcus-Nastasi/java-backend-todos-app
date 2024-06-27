package com.app.todos.Controller.Todos;

import com.app.todos.DTOs.Todos.NewTodoDTO;
import com.app.todos.DTOs.Todos.UpdStatusDTO;
import com.app.todos.DTOs.Todos.UpdateTodoDTO;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Services.Todos.TodosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/todos")
@CrossOrigin(origins = {"http://todos.rolemberg.net.br:3030", "http://3.222.141.185:3030", "http://192.168.0.76:3030"})
public class TodosController {

    @Autowired
    private TodosService todosService;

    @GetMapping(value = "/get/{id}/")
    public ResponseEntity<Todo> getSingle(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.get(id, token));
    }

    @GetMapping(value = "/all/{user_id}/")
    public ResponseEntity<List<Todo>> all(@PathVariable BigInteger user_id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getAll(user_id, token));
    }

    @GetMapping(value = "/done/{user_id}/")
    public ResponseEntity<List<Todo>> getDone(@PathVariable BigInteger user_id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getDone(user_id, token));
    }

    @GetMapping(value = "/progress/{user_id}/")
    public ResponseEntity<List<Todo>> getProgress(@PathVariable BigInteger user_id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getProgress(user_id, token));
    }

    @GetMapping(value = "/pending/{user_id}/")
    public ResponseEntity<List<Todo>> getPending(@PathVariable BigInteger user_id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(todosService.getPending(user_id, token));
    }

    @PostMapping(value = "/new/")
    public ResponseEntity<String> newTodo(@RequestBody @Validated NewTodoDTO data) {
        todosService.newTodo(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/{id}/")
    public ResponseEntity<String> update(@PathVariable BigInteger id, @RequestBody @Validated UpdateTodoDTO data, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.update(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/status/{id}/")
    public ResponseEntity<String> updateStatus(@PathVariable BigInteger id, @RequestBody UpdStatusDTO data, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.updateStatus(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}/")
    public ResponseEntity<String> delete(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.delete(id, token);
        return ResponseEntity.accepted().build();
    }
}



