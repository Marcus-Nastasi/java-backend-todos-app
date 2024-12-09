package com.app.todos.infrastructure.gateway.users;

import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.domain.users.User;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;

import java.math.BigInteger;

public class UserRepoGateway implements UserGateway {

    private final UserRepo userRepo;
    private final UserEntityMapper userEntityMapper;

    public UserRepoGateway(UserRepo userRepo, UserEntityMapper userEntityMapper) {
        this.userRepo = userRepo;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User get(BigInteger id) {
        return userEntityMapper.mapFromUserEntity(userRepo.findById(id).orElseThrow());
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User create(User data) {
        return userEntityMapper.mapFromUserEntity(userRepo.save(userEntityMapper.mapToUserEntity(data)));
    }

    @Override
    public User update(BigInteger id, User data) {
        User user = this.get(id);
        return userEntityMapper.mapFromUserEntity(userRepo.save(userEntityMapper.mapToUserEntity(user.updateDetails(data))));
    }

    @Override
    public User delete(BigInteger id) {
        User user = this.get(id);
        userRepo.deleteById(id);
        return user;
    }
}
