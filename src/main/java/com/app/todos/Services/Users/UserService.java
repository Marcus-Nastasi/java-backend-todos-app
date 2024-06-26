package com.app.todos.Services.Users;

import com.app.todos.DTOs.Users.NewUserDTO;
import com.app.todos.DTOs.Users.UpdateDTO;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public User get(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        return userRepo.findById(id).orElseThrow();
    }

    public void newUser(NewUserDTO data) {
        String encoded = passwordEncoder.encode(data.password());
        User u = new User(data.name(), data.email(), encoded);
        userRepo.save(u);
    }

    public void update(BigInteger id, UpdateDTO data, String token) {
        User u = userRepo.findById(id).orElseThrow();

        if (!tokenService.validate(token).equals(u.getEmail())) return;
        if (!passwordEncoder.matches(data.currentPassword(), u.getPassword())) return;

        String encoded = passwordEncoder.encode(data.newPassword());

        u.setName(data.name());
        u.setEmail(data.email());
        u.setPassword(encoded);

        userRepo.save(u);
    }

    public void delete(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return;
        userRepo.deleteById(id);
    }
}



