package com.app.todos.application.service.users;

import com.app.todos.application.service.auth.TokenService;
import com.app.todos.domain.users.DTOs.UserRequestDTO;
import com.app.todos.domain.users.DTOs.UserUpdateDTO;
import com.app.todos.domain.users.User;
import com.app.todos.web.handler.exception.AppException;
import com.app.todos.web.handler.exception.ForbiddenException;
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

    public User validateUserToken(BigInteger user_id, String token) {
        User u = userRepo
            .findById(user_id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return u;
    }

    public User get(BigInteger id, String token) {
        return this.validateUserToken(id, token);
    }

    public User newUser(UserRequestDTO data) {
        if (data.name().isEmpty() || data.email().isEmpty() || data.password().isEmpty())
            throw new AppException("name, email or password cannot be null");
        if (userRepo.findByEmail(data.email()) != null) 
            throw new AppException("e-mail already exists");
        String encoded = passwordEncoder.encode(data.password());
        User u = new User(data.name(), data.email(), encoded);
        userRepo.save(u);
        return u;
    }

    public User update(BigInteger id, UserUpdateDTO data, String token) {
        if (
            data.name().isEmpty()
            || data.email().isEmpty()
            || data.currentPassword().isEmpty()
            || data.newPassword().isEmpty()
        ) {
            throw new AppException("name, email or password cannot be null");
        }
        User u = this.validateUserToken(id, token);
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
        User u = this.validateUserToken(id, token);
        userRepo.deleteById(id);
        return u;
    }
}
