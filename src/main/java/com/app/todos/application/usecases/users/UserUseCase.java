package com.app.todos.application.usecases.users;

import com.app.todos.application.gateway.security.TokenGateway;
import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.application.usecases.security.PasswordUseCase;
import com.app.todos.domain.users.User;
import com.app.todos.application.exception.AppException;
import com.app.todos.application.exception.ForbiddenException;

import java.math.BigInteger;

public class UserUseCase {
    private final UserGateway userGateway;
    private final PasswordUseCase passwordUseCase;
    private final TokenGateway tokenGateway;

    public UserUseCase(UserGateway userGateway, PasswordUseCase passwordGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.passwordUseCase = passwordGateway;
        this.tokenGateway = tokenGateway;
    }

    public User get(BigInteger id, String token) {
        User user = userGateway.get(id);
        if (user == null) throw new AppException("user not found");
        if (!tokenGateway.validate(token).equals(user.getEmail()))
            throw new ForbiddenException("Invalid token");
        return user;
    }

    public User validateUserToken(BigInteger user_id, String token) {
        User user = userGateway.get(user_id);
        if (user == null) throw new AppException("user not found");
        if (!tokenGateway.validate(token).equals(user.getEmail()))
            throw new ForbiddenException("Invalid token");
        return user;
    }

    public User create(User data) {
        if (data.getName().isEmpty() || data.getEmail().isEmpty() || data.getPassword().isEmpty())
            throw new AppException("name, email or password cannot be null");
        if (userGateway.findByEmail(data.getEmail()) != null)
            throw new AppException("e-mail already exists");
        data.setPassword(passwordUseCase.encode(data.getPassword()));
        userGateway.create(data);
        return data;
    }

    public User update(BigInteger id, User data, String currentPassword, String token) {
        if (data.getName().isEmpty() || data.getEmail().isEmpty() || data.getPassword().isEmpty())
            throw new AppException("name, email or password cannot be null");
        User user = get(id, token);
        if (user == null) throw new AppException("user not found");
        if (!passwordUseCase.matches(currentPassword, user.getPassword()))
            throw new ForbiddenException("Invalid password");
        data.setPassword(passwordUseCase.encode(data.getPassword()));
        return userGateway.update(id, user.updateDetails(data));
    }

    public User delete(BigInteger id, String token) {
        User user = get(id, token);
        if (user == null) throw new AppException("user not found");
        userGateway.delete(id);
        return user;
    }
}
