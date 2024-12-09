package com.app.todos.application.gateway.users;

import com.app.todos.domain.users.User;

import java.math.BigInteger;

public interface UserGateway {
    User get(BigInteger id);
    User findByEmail(String email);
    User create(User data);
    User update(BigInteger id, User data);
    User delete(BigInteger id);
}
