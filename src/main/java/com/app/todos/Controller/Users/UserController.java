package com.app.todos.Controller.Users;

import com.app.todos.DTOs.Users.NewUserDTO;
import com.app.todos.Services.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/new/")
    public ResponseEntity<String> newUser(@RequestBody @Validated NewUserDTO data) {
        userService.newUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }
}


