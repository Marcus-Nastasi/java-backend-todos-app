package com.app.todos.adapters.mapper;

import com.app.todos.adapters.input.users.UserRequestDTO;
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
}
