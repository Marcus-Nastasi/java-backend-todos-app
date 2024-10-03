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
@CrossOrigin(origins = {"http://192.168.0.76:3030", "http://localhost:3030"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<User> get(
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(userService.get(id, token));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<User> newUser(
            @RequestBody @Validated NewUserDTO data
    ) {
        User u = userService.newUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> update(
            @RequestBody @Validated UpdateDTO data,
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        User u = userService.update(id, data, token);
        return ResponseEntity.accepted().body(u);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> del(
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        User u = userService.delete(id, token);
        return ResponseEntity.accepted().body(u);
    }
}
