package com.app.todos.infrastructure.mapper.user;

import com.app.todos.domain.users.User;
import com.app.todos.infrastructure.entity.users.UserEntity;

public class UserEntityMapper {
    public UserEntity mapToUserEntity(User user) {
        return new UserEntity(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );
    }

    public User mapFromUserEntity(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getEmail(),
            userEntity.getPassword()
        );
    }
}
