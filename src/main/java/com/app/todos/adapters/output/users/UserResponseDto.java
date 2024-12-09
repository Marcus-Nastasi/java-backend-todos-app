package com.app.todos.adapters.output.users;

import java.io.Serializable;
import java.math.BigInteger;

public record UserResponseDto(
        BigInteger id,
        String name,
        String email,
        String password
) implements Serializable {}
