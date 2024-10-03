package com.app.todos;

import com.app.todos.domain.Users.DTOs.NewUserDTO;
import com.app.todos.domain.Users.DTOs.UpdateDTO;
import com.app.todos.domain.Users.User;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.Auth.TokenService;
import com.app.todos.application.service.Users.UserService;
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

    @Test
    void getUser() {
        User user = new User("Brian", "brian@gmail.com", "12345");
        String token = "token";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        assertDoesNotThrow(() -> {
            userService.get(BigInteger.valueOf(2000), token);
        });
        assertEquals(userService.get(BigInteger.valueOf(2000), token), user);
    }

    @Test
    void newUserTest() {
        User user = new User("Brian", "brian@gmail.com", "12345");
        NewUserDTO newUserDTO = new NewUserDTO("Brian", "brian@gmail.com", "12345");

        when(userRepo.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> {
            userService.newUser(newUserDTO);
        });
    }

    @Test
    void update() {
        User user = new User("Brian", "brian@gmail.com", "12345");
        UpdateDTO updateDTO = new UpdateDTO("New Brian", "new email", "12345", "new pass");
        String token = "token";

        when(userRepo.save(any(User.class))).thenReturn(user);
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(passwordEncoder.matches(updateDTO.currentPassword(), user.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> {
            userService.update(BigInteger.valueOf(2500), updateDTO, token);
        });
    }

    @Test
    void deleteUser() {
        User user = new User("Brian", "brian@gmail.com", "12345");
        String token = "token";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        assertDoesNotThrow(() -> {
            userService.delete(BigInteger.valueOf(1005), token);
        });
    }
}



