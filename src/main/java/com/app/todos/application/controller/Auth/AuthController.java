package com.app.todos.application.controller.Auth;

import com.app.todos.domain.Auth.DTOs.AuthRequestDTO;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.Auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = {"http://192.168.0.76:3030", "http://localhost:3030"})
public class AuthController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody @Valid AuthRequestDTO data
    ) {
        var auth = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        authenticationManager.authenticate(auth);
        UserDetails u = userRepo.findByEmail(data.email());
        Map<String, String> res = Map.of("token", tokenService.generate(u.getUsername()));
        return(
            passwordEncoder.matches(data.password(), u.getPassword()) ?
            ResponseEntity.accepted().body(res) : ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        );
    }
}
