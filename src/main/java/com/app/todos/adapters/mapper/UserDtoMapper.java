package com.app.todos.adapters.mapper;

import com.app.todos.adapters.input.users.UserRequestDTO;
import com.app.todos.adapters.input.users.UserUpdateDTO;
import com.app.todos.adapters.output.users.UserResponseDto;
import com.app.todos.domain.users.User;

public class UserDtoMapper {
    public UserResponseDto mapToResponse(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );
    }

    public User mapFromRequest(UserRequestDTO userRequestDTO) {
        return new User(
            null,
            userRequestDTO.name(),
            userRequestDTO.email(),
            userRequestDTO.password()
        );
    }

    public User mapFromUpdate(UserUpdateDTO userUpdateDTO) {
        return new User(
            null,
            userUpdateDTO.name(),
            userUpdateDTO.email(),
            userUpdateDTO.newPassword()
        );
    }
}
