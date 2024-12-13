package com.app.todos;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.adapters.input.todos.TodosStatusDTO;
import com.app.todos.domain.todos.Priority;
import com.app.todos.domain.todos.Status;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.todos.TodosPage;
import com.app.todos.domain.users.User;
import com.app.todos.application.usecases.todos.TodosUseCase;
import com.app.todos.application.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodosServiceTests {

    @Mock
    private TodosGateway todosGateway;
    @Mock
    private UserUseCase userUseCase;
    @InjectMocks
    private TodosUseCase todosService;

    Todo todo = new Todo(BigInteger.valueOf(1), BigInteger.valueOf(1), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.now(), LocalDate.of(2024, 7, 15), Status.PENDING, Priority.HIGH, LocalDate.now());
    Todo todo2 = new Todo(BigInteger.valueOf(1), BigInteger.valueOf(1), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.now(), LocalDate.of(2024, 7, 15), Status.PENDING, Priority.LOW, LocalDate.now());
    User user = new User(BigInteger.valueOf(1), "Brian", "bian@gmail.com", "12345");
    List<Todo> todos = new ArrayList<>();
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodo() {
        when(todosGateway.get(any(BigInteger.class))).thenReturn(todo);
        when(userUseCase.validateUserToken(any(BigInteger.class), eq(token))).thenReturn(user);
        when(userUseCase.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);

        assertDoesNotThrow(() -> todosService.get(BigInteger.valueOf(1500), token));
        assertEquals(todo, todosService.get(BigInteger.valueOf(1500), token));
        assertEquals(
            todo.getClient(),
            todosService.get(BigInteger.valueOf(1500), token).getClient()
        );
        assertThrows(ForbiddenException.class, () -> {
            todosService.get(BigInteger.valueOf(1500), falseToken);
        });

        verify(todosGateway, times(4)).get(any(BigInteger.class));
        verify(userUseCase, times(4)).validateUserToken(any(BigInteger.class), any(String.class));
    }

    @Test
    void getAll() {
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 1);
        TodosPage todosPage = new TodosPage(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userUseCase.validateUserToken(user.getId(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(user.getId(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.getAll(
            user.getId(),
            "",
            "",
            0,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        )).thenReturn(todosPage);

        assertEquals(todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "0",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ), todosPage);
        assertEquals(todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "0",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ).getData(), todosPage.getData());
        assertDoesNotThrow(() -> todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "0",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ));
        assertThrows(
            ForbiddenException.class,
            () -> todosService.getAll(
                user.getId(),
                falseToken,
                "",
                "",
                "0",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            )
        );

        verify(todosGateway, times(3)).getAll(
            user.getId(),
            "",
            "",
            0,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        );
        verify(userUseCase, times(3)).validateUserToken(user.getId(), token);
        verify(userUseCase, times(1)).validateUserToken(user.getId(), falseToken);
    }

    @Test
    void getDone() {
        todo.setStatus(Status.DONE);
        todo2.setStatus(Status.DONE);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPage todosPage = new TodosPage(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userUseCase.validateUserToken(user.getId(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(user.getId(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.getAll(
            user.getId(),
            "",
            "",
            2,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        )).thenReturn(todosPage);

        assertEquals(todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "2",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ), todosPage);
        assertEquals(
            todosService.getAll(
                user.getId(),
                token,
                "",
                "",
                "2",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            ).getData().getFirst().getStatus(),
            todosPage.getData().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "2",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ));
        assertThrows(
            ForbiddenException.class,
            () -> todosService.getAll(
                user.getId(),
                falseToken,
                "",
                "",
                "2",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            )
        );

        verify(todosGateway, times(3)).getAll(
            user.getId(),
            "",
            "",
            2,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        );
        verify(userUseCase, times(3)).validateUserToken(user.getId(), token);
        verify(userUseCase, times(1)).validateUserToken(user.getId(), falseToken);
    }

    @Test
    void getProgress() {
        todo.setStatus(Status.PROGRESS);
        todo2.setStatus(Status.PROGRESS);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPage todosPage = new TodosPage(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userUseCase.validateUserToken(user.getId(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(user.getId(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.getAll(
            user.getId(),
            "",
            "",
            1,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        )).thenReturn(todosPage);

        assertEquals(todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "1",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ), todosPage);
        assertEquals(
            todosService.getAll(
                user.getId(),
                token,
                "",
                "",
                "1",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            ).getData().getFirst().getStatus(),
            todosPage.getData().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "1",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ));
        assertThrows(
            ForbiddenException.class,
            () -> todosService.getAll(
                user.getId(),
                falseToken,
                "",
                "",
                "1",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            )
        );

        verify(todosGateway, times(3)).getAll(
            user.getId(),
            "",
            "",
            1,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        );
        verify(userUseCase, times(3)).validateUserToken(user.getId(), token);
        verify(userUseCase, times(1)).validateUserToken(user.getId(), falseToken);
    }

    @Test
    void getPending() {
        todo.setStatus(Status.PENDING);
        todo2.setStatus(Status.PENDING);
        todos.add(todo);
        todos.add(todo2);
        Page<Todo> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPage todosPage = new TodosPage(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userUseCase.validateUserToken(user.getId(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(user.getId(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.getAll(
            user.getId(),
            "",
            "",
            0,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        )).thenReturn(todosPage);

        assertEquals(todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "0",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ), todosPage);
        assertEquals(
            todosService.getAll(
                user.getId(),
                token,
                "",
                "",
                "0",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            ).getData().getFirst().getStatus(),
            todosPage.getData().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            user.getId(),
            token,
            "",
            "",
            "0",
            "1",
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        ));
        assertThrows(
            ForbiddenException.class,
            () -> todosService.getAll(
                user.getId(),
                falseToken,
                "",
                "",
                "0",
                "1",
                LocalDate.of(1900, 1, 1),
                LocalDate.now(),
                LocalDate.now(),
                0,
                10
            )
        );

        verify(todosGateway, times(3)).getAll(
            user.getId(),
            "",
            "",
            0,
            1,
            LocalDate.of(1900, 1, 1),
            LocalDate.now(),
            LocalDate.now(),
            0,
            10
        );
        verify(userUseCase, times(3)).validateUserToken(user.getId(), token);
        verify(userUseCase, times(1)).validateUserToken(user.getId(), falseToken);
    }

    @Test
    void create() {
        when(todosGateway.create(any(Todo.class))).thenReturn(todo);

        assertEquals(todo.getId(), todosService.newTodo(todo).getId());
        assertDoesNotThrow(() -> todosService.newTodo(todo2));

        verify(todosGateway, times(2)).create(any(Todo.class));
    }

    @Test
    void updateTodo() {
        when(todosGateway.get(todo.getId())).thenReturn(todo);
        when(todosGateway.update(todo.getId(), todo)).thenReturn(todo);
        when(userUseCase.validateUserToken(todo.getUser_id(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(todo.getUser_id(), falseToken)).thenThrow(ForbiddenException.class);

        assertEquals(
            todo.getDescription(),
            todosService.update(todo.getId(), todo, token).getDescription()
        );
        assertDoesNotThrow(() -> {
            todosService.update(todo.getId(), todo, token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.update(todo.getId(), todo, falseToken);
        });

        verify(todosGateway, times(3)).get(todo.getId());
        verify(todosGateway, times(2)).update(todo.getId(), todo);
        verify(userUseCase, times(2)).validateUserToken(todo.getUser_id(), token);
        verify(userUseCase, times(1)).validateUserToken(todo.getUser_id(), falseToken);
    }

    @Test
    void updateStatus() {
        TodosStatusDTO statusDTO = new TodosStatusDTO(Status.DONE);

        when(userUseCase.validateUserToken(todo.getUser_id(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(todo.getUser_id(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.get(todo.getId())).thenReturn(todo);
        when(todosGateway.update(todo.getId(), todo)).thenReturn(todo);

        assertEquals(todo, todosService.updateStatus(todo.getId(), statusDTO.status(), token));
        assertEquals(
            todo.getStatus(),
            todosService.update(todo.getId(), todo, token).getStatus()
        );
        assertDoesNotThrow(() -> {
            todosService.updateStatus(todo.getId(), statusDTO.status(), token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.updateStatus(todo.getId(), statusDTO.status(), falseToken);
        });

        verify(userUseCase, times(4)).validateUserToken(any(BigInteger.class), any(String.class));
        verify(todosGateway, times(4)).get(any(BigInteger.class));
        verify(todosGateway, times(3)).update(any(BigInteger.class), any(Todo.class));
    }

    @Test
    void deleteTodo() {
        when(userUseCase.validateUserToken(todo.getUser_id(), token)).thenReturn(user);
        when(userUseCase.validateUserToken(todo.getUser_id(), falseToken)).thenThrow(ForbiddenException.class);
        when(todosGateway.get(todo.getId())).thenReturn(todo);
        when(todosGateway.delete(todo.getId())).thenReturn(todo);

        assertEquals(todo.getDescription(), todosService.delete(todo.getId(), token).getDescription());
        assertEquals(
            todo.getPriority(),
            todosService.delete(todo.getId(), token)
                .getPriority()
        );
        assertDoesNotThrow(() -> {
            todosService.delete(todo.getId(), token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.delete(todo.getId(), falseToken);
        });

        verify(userUseCase, times(3)).validateUserToken(todo.getId(), token);
        verify(userUseCase, times(1)).validateUserToken(todo.getId(), falseToken);
        verify(todosGateway, times(4)).get(any(BigInteger.class));
        verify(todosGateway, times(3)).delete(any(BigInteger.class));
    }
}
