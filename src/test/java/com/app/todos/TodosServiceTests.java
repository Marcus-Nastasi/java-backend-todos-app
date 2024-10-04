package com.app.todos;

import com.app.todos.domain.Todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.Todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.Todos.DTOs.TodosUpdateDTO;
import com.app.todos.resources.enums.todos.Priority;
import com.app.todos.resources.enums.todos.Status;
import com.app.todos.domain.Todos.Todo;
import com.app.todos.domain.Users.User;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.Auth.TokenService;
import com.app.todos.application.service.Todos.TodosService;
import com.app.todos.application.service.Users.UserService;
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

    @Test
    void getDone() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        todo.setStatus(Status.DONE);
        Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.LOW);
        todo2.setStatus(Status.DONE);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        todos.add(todo2);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.getDone(any(BigInteger.class))).thenReturn(todos);

        assertEquals(todosService.getDone(BigInteger.valueOf(1500), token), todos);
        assertDoesNotThrow(() -> {
            todosService.getDone(BigInteger.valueOf(1500), token);
        });
        assertEquals(Status.DONE, todosService.getDone(BigInteger.valueOf(1500), token).getFirst().getStatus());

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.getDone(BigInteger.valueOf(1500), falseToken);
        });
    }

    @Test
    void getProgress() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        todo.setStatus(Status.PROGRESS);
        Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.LOW);
        todo2.setStatus(Status.PROGRESS);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        todos.add(todo2);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.getInProgress(any(BigInteger.class))).thenReturn(todos);

        assertEquals(todosService.getProgress(BigInteger.valueOf(1500), token), todos);
        assertDoesNotThrow(() -> {
            todosService.getProgress(BigInteger.valueOf(1500), token);
        });
        assertEquals(Status.PROGRESS, todosService.getProgress(BigInteger.valueOf(1500), token).getFirst().getStatus());

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.getProgress(BigInteger.valueOf(1500), falseToken);
        });
    }

    @Test
    void getPending() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        todo.setStatus(Status.PENDING);
        Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.LOW);
        todo2.setStatus(Status.PENDING);
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        todos.add(todo2);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.getPending(any(BigInteger.class))).thenReturn(todos);

        assertEquals(todosService.getPending(BigInteger.valueOf(1500), token), todos);
        assertDoesNotThrow(() -> {
            todosService.getPending(BigInteger.valueOf(1500), token);
        });
        assertEquals(Status.PENDING, todosService.getPending(BigInteger.valueOf(1500), token).getFirst().getStatus());

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.getPending(BigInteger.valueOf(1500), falseToken);
        });
    }

    @Test
    void newTodo() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        TodosRequestDTO todoDTO = new TodosRequestDTO(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);

        when(todosRepo.save(any(Todo.class))).thenReturn(todo);

        assertDoesNotThrow(() -> {
            todosService.newTodo(todoDTO);
        });
    }

    @Test
    void updateTodo() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        TodosUpdateDTO todoDTO = new TodosUpdateDTO("Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.save(any(Todo.class))).thenReturn(todo);

        assertDoesNotThrow(() -> {
            todosService.update(BigInteger.valueOf(1500), todoDTO, token);
        });

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.update(BigInteger.valueOf(1500), todoDTO, falseToken);
        });
    }

    @Test
    void updateStatus() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        TodosStatusDTO statusDTO = new TodosStatusDTO(Status.DONE);
        User user = new User("Brian", "bian@gmail.com", "12345");
        User user2 = new User("Brian", "bianFalse@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.save(any(Todo.class))).thenReturn(todo);

        assertDoesNotThrow(() -> {
            todosService.updateStatus(BigInteger.valueOf(1500), statusDTO, token);
        });

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        assertThrows(JWTVerificationException.class, () -> {
            todosService.updateStatus(BigInteger.valueOf(1000), statusDTO, falseToken);
        });
    }

    @Test
    void deleteTodo() {
        Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
        User user = new User("Brian", "bian@gmail.com", "12345");
        String token = "Token";
        String falseToken = "Token False";

        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());

        assertDoesNotThrow(() -> {
            todosService.delete(BigInteger.valueOf(1500), token);
        });

        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);

        assertThrows(JWTVerificationException.class, () -> {
            todosService.delete(BigInteger.valueOf(1000), falseToken);
        });
    }
}



