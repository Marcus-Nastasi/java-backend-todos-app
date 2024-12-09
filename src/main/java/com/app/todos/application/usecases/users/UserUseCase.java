package com.app.todos.application.usecases.users;

import com.app.todos.application.gateway.auth.PasswordGateway;
import com.app.todos.application.gateway.auth.TokenGateway;
import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.domain.users.User;
import com.app.todos.application.exception.AppException;
import com.app.todos.application.exception.ForbiddenException;

import java.math.BigInteger;

public class UserUseCase {

    private final UserGateway userGateway;
    private final PasswordGateway passwordGateway;
    private final TokenGateway tokenGateway;

    public UserUseCase(UserGateway userGateway, PasswordGateway passwordGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.passwordGateway = passwordGateway;
        this.tokenGateway = tokenGateway;
    }

    public User get(BigInteger id, String token) {
        User u = userGateway.get(id);
        if (!tokenGateway.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return u;
    }

    public User validateUserToken(BigInteger user_id, String token) {
        User u = userGateway.get(user_id);
        if (!tokenGateway.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return u;
    }

    public User create(User data) {
        if (data.getName().isEmpty() || data.getEmail().isEmpty() || data.getPassword().isEmpty())
            throw new AppException("name, email or password cannot be null");
        if (userGateway.findByEmail(data.getEmail()) != null)
            throw new AppException("e-mail already exists");
        String encoded = passwordGateway.encode(data.getPassword());
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
        if (!passwordGateway.matches(data.getPassword(), u.getPassword()))
            throw new ForbiddenException("Invalid password");
         */
        User updated = u.updateDetails(data);
        String encoded = passwordGateway.encode(data.getPassword());
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
