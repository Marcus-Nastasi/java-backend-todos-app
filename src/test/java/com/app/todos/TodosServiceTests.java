package com.app.todos;

import com.app.todos.domain.todos.DTOs.TodosPageResponseDto;
import com.app.todos.domain.todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.todos.DTOs.TodosUpdateDTO;
import com.app.todos.resources.enums.todos.Priority;
import com.app.todos.resources.enums.todos.Status;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.users.User;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.todos.TodosService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private UserRepo userRepo;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private TodosService todosService;

    Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
    Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.LOW);
    User user = new User("Brian", "bian@gmail.com", "12345");
    TodosRequestDTO todoDTO = new TodosRequestDTO(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
    TodosUpdateDTO todoUpdateDTO = new TodosUpdateDTO("Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 07, 15), Priority.HIGH);
    List<Todo> todos = new ArrayList<>();
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodo() {
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
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        when(
            todosRepo.getUserTodos(
                any(BigInteger.class),
                any(String.class),
                any(Integer.class),
                any(Pageable.class)
            )
        ).thenReturn(todoPage);

        assertEquals(
            todosService.getAll(BigInteger.valueOf(1500), token, "", "0", 0, 10),
            todosPageResponseDto
        );
        assertDoesNotThrow(() -> {
            todosService.getAll(BigInteger.valueOf(1500), token, "", "0", 0, 10);
        });
        assertThrows(JWTVerificationException.class, () -> {
            todosService.getAll(BigInteger.valueOf(1500), falseToken, "", "0", 0, 10);
        });
    }

    @Test
    void getDone() {
        todo.setStatus(Status.DONE);
        todo2.setStatus(Status.DONE);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        when(todosRepo.getDone(any(BigInteger.class), any(Pageable.class))).thenReturn(todoPage);

        assertEquals(
            todosService.getDone(BigInteger.valueOf(1500), token, 0, 10),
            todosPageResponseDto
        );
        assertDoesNotThrow(() -> {
            todosService.getDone(BigInteger.valueOf(1500), token, 0, 10);
        });
        assertEquals(
            Status.DONE,
            todosService.getDone(BigInteger.valueOf(1500), token, 0, 10)
                .data()
                .getFirst()
                .getStatus()
        );
        assertThrows(JWTVerificationException.class, () -> {
            todosService.getDone(BigInteger.valueOf(1500), falseToken, 0, 10);
        });
    }

    @Test
    void getProgress() {
        todo.setStatus(Status.PROGRESS);
        todo2.setStatus(Status.PROGRESS);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
                todoPage.getPageable().getPageNumber(),
                todoPage.getPageable().getPageSize(),
                todoPage.getTotalPages(),
                todos
        );

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        when(todosRepo.getInProgress(any(BigInteger.class), any(Pageable.class))).thenReturn(todoPage);

        assertEquals(
            todosService.getProgress(BigInteger.valueOf(1500), token, 0, 10),
            todosPageResponseDto
        );
        assertDoesNotThrow(() -> {
            todosService.getProgress(BigInteger.valueOf(1500), token, 0, 10);
        });
        assertEquals(
            Status.PROGRESS,
            todosService.getProgress(BigInteger.valueOf(1500), token, 0, 10)
                .data()
                .getFirst()
                .getStatus()
        );
        assertThrows(JWTVerificationException.class, () -> {
            todosService.getProgress(BigInteger.valueOf(1500), falseToken, 0, 10);
        });
    }

    @Test
    void getPending() {
        todo.setStatus(Status.PENDING);
        todo2.setStatus(Status.PENDING);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
                todoPage.getPageable().getPageNumber(),
                todoPage.getPageable().getPageSize(),
                todoPage.getTotalPages(),
                todos
        );

        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        when(todosRepo.getPending(any(BigInteger.class), any(Pageable.class))).thenReturn(todoPage);

        assertEquals(
            todosService.getPending(BigInteger.valueOf(1500), token, 0, 10),
            todosPageResponseDto
        );
        assertDoesNotThrow(() -> {
            todosService.getPending(BigInteger.valueOf(1500), token, 0, 10);
        });
        assertEquals(
            Status.PENDING,
            todosService.getPending(BigInteger.valueOf(1500), token, 0, 10)
                .data()
                .getFirst()
                .getStatus()
        );
        assertThrows(JWTVerificationException.class, () -> {
            todosService.getPending(BigInteger.valueOf(1500), falseToken, 0, 10);
        });
    }

    @Test
    void newTodo() {
        when(todosRepo.save(any(Todo.class))).thenReturn(todo);
        assertDoesNotThrow(() -> todosService.newTodo(todoDTO));
    }

    @Test
    void updateTodo() {
        when(userRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(user));
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(tokenService.validate(token)).thenReturn(user.getEmail());
        when(todosRepo.save(any(Todo.class))).thenReturn(todo);
        assertDoesNotThrow(() -> {
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, token);
        });
        when(tokenService.validate(falseToken)).thenThrow(JWTVerificationException.class);
        assertThrows(JWTVerificationException.class, () -> {
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, falseToken);
        });
    }

    @Test
    void updateStatus() {
        TodosStatusDTO statusDTO = new TodosStatusDTO(Status.DONE);
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
