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
public class TodosController {

    @Autowired
    private TodosService todosService;

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @GetMapping(value = "/get/{id}/")
    public ResponseEntity<Todo> getSingle(@PathVariable BigInteger id) {
        return ResponseEntity.ok(todosService.get(id));
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @GetMapping(value = "/all/{user_id}/")
    public ResponseEntity<List<Todo>> all(@PathVariable BigInteger user_id) {
        return ResponseEntity.ok(todosService.getAll(user_id));
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @GetMapping(value = "/done/{user_id}/")
    public ResponseEntity<List<Todo>> getDone(@PathVariable BigInteger user_id) {
        return ResponseEntity.ok(todosService.getDone(user_id));
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @GetMapping(value = "/progress/{user_id}/")
    public ResponseEntity<List<Todo>> getProgress(@PathVariable BigInteger user_id) {
        return ResponseEntity.ok(todosService.getProgress(user_id));
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @GetMapping(value = "/pending/{user_id}/")
    public ResponseEntity<List<Todo>> getPending(@PathVariable BigInteger user_id) {
        return ResponseEntity.ok(todosService.getPending(user_id));
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @PostMapping(value = "/new/")
    public ResponseEntity<String> newTodo(@RequestBody @Validated NewTodoDTO data) {
        todosService.newTodo(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @PutMapping(value = "/update/{id}/")
    public ResponseEntity<String> update(@PathVariable BigInteger id, @RequestBody @Validated UpdateTodoDTO data, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.update(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @PutMapping(value = "/update/status/{id}/")
    public ResponseEntity<String> updateStatus(@PathVariable BigInteger id, @RequestBody UpdStatusDTO data, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        todosService.updateStatus(id, data, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin(value = "http://192.168.0.76:3030")
    @DeleteMapping(value = "/delete/{id}/")
    public ResponseEntity<String> delete(@PathVariable BigInteger id) {
        todosService.delete(id);
        return ResponseEntity.accepted().build();
    }
}



