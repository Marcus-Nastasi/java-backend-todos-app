package com.app.todos.adapters.controller.users;

import com.app.todos.adapters.input.users.UserRequestDTO;
import com.app.todos.adapters.input.users.UserUpdateDTO;
import com.app.todos.adapters.mapper.UserDtoMapper;
import com.app.todos.domain.users.User;
import com.app.todos.application.usecases.users.UserUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> get(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity.ok(userUseCase.get(id, token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<User> create(@RequestBody @Valid UserRequestDTO data) {
        User u = userUseCase.create(userDtoMapper.mapFromRequest(data));
        return ResponseEntity.created(URI.create("/" + u.getId())).body(u);
    }

    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<User> update(
            @RequestBody @Valid UserUpdateDTO data,
            @PathVariable BigInteger id,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        User u = userUseCase.update(id, userDtoMapper.mapFromUpdate(data), data.currentPassword(), token);
        return ResponseEntity.ok(u);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable BigInteger id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("Bearer ", "");
        User u = userUseCase.delete(id, token);
        return ResponseEntity.ok(u);
    }
}
