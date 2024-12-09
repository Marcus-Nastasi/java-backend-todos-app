package com.app.todos.adapters.controller.auth;

import com.app.todos.adapters.input.auth.AuthRequestDTO;
import com.app.todos.adapters.output.auth.AuthResponseDTO;
import com.app.todos.application.usecases.auth.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Autowired
    private AuthUseCase authUseCase;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO data) {
        return ResponseEntity.ok(
            new AuthResponseDTO(authUseCase.login(data.email(), data.password()), authUseCase.getByEmail(data.email()))
        );
    }
}
