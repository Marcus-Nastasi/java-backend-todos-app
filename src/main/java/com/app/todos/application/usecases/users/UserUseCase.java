package com.app.todos.application.usecases.users;

import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.domain.users.User;
import com.app.todos.infrastructure.gateway.auth.TokenService;
import com.app.todos.application.exception.AppException;
import com.app.todos.application.exception.ForbiddenException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;

public class UserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserUseCase(UserGateway userGateway, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public User get(BigInteger id, String token) {
        User u = userGateway.get(id);
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return u;
    }

    public User validateUserToken(BigInteger user_id, String token) {
        User u = userGateway.get(user_id);
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return u;
    }

    public User newUser(User data) {
        if (data.getName().isEmpty() || data.getEmail().isEmpty() || data.getPassword().isEmpty())
            throw new AppException("name, email or password cannot be null");
        if (userGateway.findByEmail(data.getEmail()) != null)
            throw new AppException("e-mail already exists");
        String encoded = passwordEncoder.encode(data.getPassword());
        data.setPassword(encoded);
        userGateway.create(data);
        return data;
    }

    public User update(BigInteger id, User data, String token) {
        if (data.getName().isEmpty() || data.getEmail().isEmpty() || data.getPassword().isEmpty()) {
            throw new AppException("name, email or password cannot be null");
        }
        User u = this.get(id, token);
        /*
        if (!passwordEncoder.matches(data.currentPassword(), u.getPassword()))
            throw new ForbiddenException("Invalid password");*/
        User updated = u.updateDetails(data);
        String encoded = passwordEncoder.encode(data.getPassword());
        updated.setPassword(encoded);
        userGateway.update(id, updated);
        return u;
    }

    public User delete(BigInteger id, String token) {
        User u = this.get(id, token);
        userGateway.delete(id);
        return u;
    }
}
