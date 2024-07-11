package com.app.todos;

import com.app.todos.Enums.Todos.Priority;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.Todos.TodosRepo;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import com.app.todos.Services.Todos.TodosService;
import com.app.todos.Services.Users.UserService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TodosServiceTests {

    @Mock
    private TodosRepo todosRepo;
    @Mock
    private UserService userService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private TodosService todosService;

    @Test
    void getTodo() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        assertDoesNotThrow(() -> {
            todosService.get(BigInteger.valueOf(1500), token);
        });
        assertEquals(todo, todosService.get(BigInteger.valueOf(1500), token));

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.get(BigInteger.valueOf(1500), falseToken);
        });
    }

    @Test
    void getAll() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.LOW);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        todos.add(todo2);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.getUserTodos(any(BigInteger.class))).thenReturn(todos);

        assertEquals(todosService.getAll(BigInteger.valueOf(1500), token), todos);
        assertDoesNotThrow(() -> {
            todosService.getAll(BigInteger.valueOf(1500), token);
        });

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.getAll(BigInteger.valueOf(1500), falseToken);
        });
    }
}



