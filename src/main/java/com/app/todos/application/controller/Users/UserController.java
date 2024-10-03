package com.app.todos.application.controller.Users;

import com.app.todos.domain.Users.DTOs.NewUserDTO;
import com.app.todos.domain.Users.DTOs.UpdateDTO;
import com.app.todos.domain.Users.User;
import com.app.todos.application.service.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin(origins = {"http://todos.rolemberg.net.br", "https://todos.rolemberg.net.br", "http://3.222.141.185:3030", "http://192.168.0.76:3030", "http://localhost:3030"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/get/{id}/")
    public ResponseEntity<User> get(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(userService.get(id, token));
    }

    @PostMapping(value = "/new/")
    public ResponseEntity<String> newUser(@RequestBody @Validated NewUserDTO data) {
        userService.newUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    @PutMapping(value = "/update/{id}/")
    public ResponseEntity<String> update(@RequestBody @Validated UpdateDTO data, @PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        userService.update(id, data, token);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/delete/{id}/")
    public ResponseEntity<String> del(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        userService.delete(id, token);
        return ResponseEntity.accepted().build();
    }
}


