package com.app.todos;

import com.app.todos.domain.users.DTOs.UserRequestDTO;
import com.app.todos.domain.users.DTOs.UserUpdateDTO;
import com.app.todos.domain.users.User;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.users.UserService;
import com.app.todos.web.handler.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepo userRepo;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    User user = new User("Brian", "brian@gmail.com", "12345");
    UserRequestDTO userRequestDTO = new UserRequestDTO("Brian", "brian@gmail.com", "12345");
    UserUpdateDTO userUpdateDTO = new UserUpdateDTO("New Brian", "new email", "12345", "new pass");
    String token = "token";

    @Test
    void getUser() {
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        User getFunction = userService.get(BigInteger.valueOf(2000), token);

        assertEquals(getFunction, user);
        assertEquals(getFunction.getEmail(), user.getEmail());
        assertDoesNotThrow(() -> getFunction);

        verify(userRepo, times(1)).findById(any(BigInteger.class));
        verify(tokenService, times(1)).validate(any(String.class));
    }

    @Test
    void newUserTest() {
        when(userRepo.save(any(User.class))).thenReturn(user);

        assertEquals(user.getEmail(), userService.newUser(userRequestDTO).getEmail());
        assertDoesNotThrow(() -> {
            userService.newUser(userRequestDTO);
        });

        verify(userRepo, times(2)).save(any(User.class));
    }

    @Test
    void update() {
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(passwordEncoder.matches(userUpdateDTO.currentPassword(), user.getPassword()))
            .thenReturn(true);
        when(userRepo.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> {
            userService.update(BigInteger.valueOf(2500), userUpdateDTO, token);
        });

        verify(userRepo, times(1)).findById(any(BigInteger.class));
        verify(tokenService, times(1)).validate(any(String.class));
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        assertEquals(user, userService.delete(BigInteger.valueOf(1005), token));
        assertEquals(user.getEmail(), userService.delete(BigInteger.valueOf(1005), token).getEmail());
        assertDoesNotThrow(() -> {
            userService.delete(BigInteger.valueOf(1005), token);
        });

        verify(userRepo, times(3)).findById(any(BigInteger.class));
        verify(tokenService, times(3)).validate(any(String.class));
        verify(userRepo, times(3)).deleteById(any(BigInteger.class));
    }
}
