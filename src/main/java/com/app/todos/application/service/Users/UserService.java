package com.app.todos.application.service.Users;

import com.app.todos.application.service.Auth.TokenService;
import com.app.todos.domain.Users.DTOs.UserRequestDTO;
import com.app.todos.domain.Users.DTOs.UserUpdateDTO;
import com.app.todos.domain.Users.User;
import com.app.todos.handler.exception.AppException;
import com.app.todos.handler.exception.ForbiddenException;
import com.app.todos.resources.repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
        User u = userRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Could not find the user"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new AppException("Invalid token");
        return u;
    }

    public User newUser(UserRequestDTO data) {
        String encoded = passwordEncoder.encode(data.password());
        User u = new User(data.name(), data.email(), encoded);
        userRepo.save(u);
        return u;
    }

    public User update(BigInteger id, UserUpdateDTO data, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        if (!passwordEncoder.matches(data.currentPassword(), u.getPassword()))
            throw new ForbiddenException("Invalid password");
        String encoded = passwordEncoder.encode(data.newPassword());
        u.setName(data.name());
        u.setEmail(data.email());
        u.setPassword(encoded);
        userRepo.save(u);
        return u;
    }

    public User delete(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        userRepo.deleteById(id);
        return u;
    }
}
