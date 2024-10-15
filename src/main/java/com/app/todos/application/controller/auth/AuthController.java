package com.app.todos.application.controller.auth;

import com.app.todos.domain.auth.dtos.AuthRequestDTO;
import com.app.todos.domain.auth.dtos.AuthResponseDTO;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
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
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid AuthRequestDTO data
    ) {
        var auth = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        authenticationManager.authenticate(auth);
        UserDetails u = userRepo.findByEmail(data.email());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(tokenService.generate(u.getUsername()), u);
        return passwordEncoder.matches(data.password(), u.getPassword())
            ? ResponseEntity.ok(authResponseDTO)
            : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
