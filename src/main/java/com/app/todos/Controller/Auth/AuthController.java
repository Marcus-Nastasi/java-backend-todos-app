package com.app.todos.Controller.Auth;

import com.app.todos.DTOs.Auth.LoginDTO;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/login/")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO data) {
        var auth = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        authenticationManager.authenticate(auth);
        UserDetails u = userRepo.findByEmail(data.email());

        return(
            passwordEncoder.matches(data.password(), u.getPassword()) ?
            ResponseEntity.accepted().body(tokenService.generate(u.getUsername())) : ResponseEntity.badRequest().build()
        );
    }

}





