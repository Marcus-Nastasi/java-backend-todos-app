package com.app.todos;

import com.app.todos.DTOs.Users.NewUserDTO;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import com.app.todos.Services.Users.UserService;
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
}



