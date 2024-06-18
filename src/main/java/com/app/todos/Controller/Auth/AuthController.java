package com.app.todos.Controller.Auth;

import com.app.todos.DTOs.Auth.LoginDTO;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @CrossOrigin(value = "http://localhost:3030")
    @PostMapping(value = "/login/")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO data) {
        var auth = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        authenticationManager.authenticate(auth);
        UserDetails u = userRepo.findByEmail(data.email());
        User uid = userRepo.findByUsername(u.getUsername());

        String res = "{ \"token\": \"" + tokenService.generate(u.getUsername()) + "\", \"uid\": \"" + uid.getId() + "\"}";

        return(
            passwordEncoder.matches(data.password(), u.getPassword()) ?
            ResponseEntity.accepted().body(res) : ResponseEntity.badRequest().build()
        );
    }
}





