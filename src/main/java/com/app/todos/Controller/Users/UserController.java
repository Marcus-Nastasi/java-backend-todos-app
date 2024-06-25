package com.app.todos.Controller.Users;

import com.app.todos.DTOs.Users.NewUserDTO;
import com.app.todos.DTOs.Users.UpdateDTO;
import com.app.todos.Models.Users.User;
import com.app.todos.Services.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(value = "http://127.0.0.1:3030")
    @GetMapping(value = "/get/{id}/")
    public ResponseEntity<User> get(@PathVariable BigInteger id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @CrossOrigin(value = "http://127.0.0.1:3030")
    @PostMapping(value = "/new/")
    public ResponseEntity<String> newUser(@RequestBody @Validated NewUserDTO data) {
        userService.newUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    @CrossOrigin(value = "http://127.0.0.1:3030")
    @PutMapping(value = "/update/{id}/")
    public ResponseEntity<String> update(@RequestBody @Validated UpdateDTO data, @PathVariable BigInteger id) {
        userService.update(id, data);
        return ResponseEntity.accepted().build();
    }

    @CrossOrigin(value = "http://127.0.0.1:3030")
    @DeleteMapping(value = "/delete/{id}/")
    public ResponseEntity<String> del(@PathVariable BigInteger id) {
        userService.delete(id);
        return ResponseEntity.accepted().build();
    }
}


