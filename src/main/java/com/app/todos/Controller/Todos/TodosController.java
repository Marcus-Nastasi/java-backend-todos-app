package com.app.todos.Controller.Todos;

import com.app.todos.DTOs.Todos.NewTodoDTO;
import com.app.todos.DTOs.Todos.UpdateTodoDTO;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Services.Todos.TodosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "/api/todos")
public class TodosController {

    @Autowired
    private TodosService todosService;

    @GetMapping(value = "/get/{id}/")
    public ResponseEntity<Todo> getSingle(@PathVariable BigInteger id) {
        return ResponseEntity.ok(todosService.get(id));
    }

    @PostMapping(value = "/new/")
    public ResponseEntity<String> newTodo(@RequestBody @Validated NewTodoDTO data) {
        todosService.newTodo(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/update/{id}/")
    public ResponseEntity<String> update(@PathVariable BigInteger id, @RequestBody @Validated UpdateTodoDTO data) {
        todosService.update(id, data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}/")
    public ResponseEntity<String> delete(@PathVariable BigInteger id) {
        todosService.delete(id);
        return ResponseEntity.accepted().build();
    }
}



