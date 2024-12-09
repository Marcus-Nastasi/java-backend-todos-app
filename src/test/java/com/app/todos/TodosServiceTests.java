package com.app.todos;

import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.adapters.output.todos.TodosPageResponseDto;
import com.app.todos.adapters.input.todos.TodosRequestDTO;
import com.app.todos.adapters.input.todos.TodosStatusDTO;
import com.app.todos.adapters.input.todos.TodosUpdateDTO;
import com.app.todos.domain.todos.Priority;
import com.app.todos.domain.todos.Status;
import com.app.todos.infrastructure.entity.todos.TodoEntity;
import com.app.todos.infrastructure.entity.users.UserEntity;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import com.app.todos.infrastructure.gateway.auth.TokenProvider;
import com.app.todos.application.usecases.todos.TodosUseCase;
import com.app.todos.application.exception.ForbiddenException;
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
    private TokenProvider tokenService;
    @Mock
    private UserUseCase userService;
    @InjectMocks
    private TodosUseCase todosService;

    TodoEntity todo = new TodoEntity(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.now(), LocalDate.of(2024, 7, 15), Priority.HIGH, LocalDate.now());
    TodoEntity todo2 = new TodoEntity(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.now(), LocalDate.of(2024, 7, 15), Priority.LOW, LocalDate.now());
    UserEntity user = new UserEntity("Brian", "bian@gmail.com", "12345");
    TodosRequestDTO todoDTO = new TodosRequestDTO(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 7, 15), Priority.HIGH);
    TodosUpdateDTO todoUpdateDTO = new TodosUpdateDTO("Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 7, 15), Priority.HIGH);
    List<TodoEntity> todos = new ArrayList<>();
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodo() {
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
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

        verify(todosRepo, times(4)).findById(any(BigInteger.class));
        verify(userService, times(4))
            .validateUserToken(any(BigInteger.class), any(String.class));
    }

    @Test
    void getAll() {
        todos.add(todo);
        todos.add(todo2);
        Page<TodoEntity> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);
        when(todosRepo.getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        )).thenReturn(todoPage);

        assertEquals(todosService.getAll(
            BigInteger.valueOf(1500),
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
        ), todosPageResponseDto);
        assertEquals(todosService.getAll(
            BigInteger.valueOf(1500),
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
        ).data(), todosPageResponseDto.data());
        assertDoesNotThrow(() -> todosService.getAll(
            BigInteger.valueOf(1500),
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
                BigInteger.valueOf(1500),
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

        verify(todosRepo, times(3)).getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        );
        verify(userService, times(4)).validateUserToken(
            any(BigInteger.class), any(String.class)
        );
    }

    @Test
    void getDone() {
        todo.setStatus(Status.DONE);
        todo2.setStatus(Status.DONE);
        todos.add(todo);
        todos.add(todo2);
        Page<TodoEntity> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);
        when(todosRepo.getUserTodos(
                any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        )).thenReturn(todoPage);

        assertEquals(todosService.getAll(
            BigInteger.valueOf(1500),
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
        ), todosPageResponseDto);
        assertEquals(
            todosService.getAll(
                BigInteger.valueOf(1500),
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
            ).data().getFirst().getStatus(),
            todosPageResponseDto.data().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            BigInteger.valueOf(1500),
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
                BigInteger.valueOf(1500),
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

        verify(todosRepo, times(3)).getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        );
        verify(userService, times(4)).validateUserToken(
            any(BigInteger.class), any(String.class)
        );
    }

    @Test
    void getProgress() {
        todo.setStatus(Status.PROGRESS);
        todo2.setStatus(Status.PROGRESS);
        todos.add(todo);
        todos.add(todo2);
        Page<TodoEntity> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);
        when(todosRepo.getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        )).thenReturn(todoPage);

        assertEquals(todosService.getAll(
            BigInteger.valueOf(1500),
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
        ), todosPageResponseDto);
        assertEquals(
            todosService.getAll(
                BigInteger.valueOf(1500),
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
            ).data().getFirst().getStatus(),
            todosPageResponseDto.data().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            BigInteger.valueOf(1500),
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
                BigInteger.valueOf(1500),
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

        verify(todosRepo, times(3)).getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        );
        verify(userService, times(4)).validateUserToken(
            any(BigInteger.class), any(String.class)
        );
    }

    @Test
    void getPending() {
        todo.setStatus(Status.PENDING);
        todo2.setStatus(Status.PENDING);
        todos.add(todo);
        todos.add(todo2);
        Page<TodoEntity> todoPage = new PageImpl<>(todos, PageRequest.of(0, 10), 2);
        TodosPageResponseDto todosPageResponseDto = new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todos
        );

        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);
        when(todosRepo.getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        )).thenReturn(todoPage);

        assertEquals(todosService.getAll(
            BigInteger.valueOf(1500),
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
        ), todosPageResponseDto);
        assertEquals(
            todosService.getAll(
                BigInteger.valueOf(1500),
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
            ).data().getFirst().getStatus(),
            todosPageResponseDto.data().getFirst().getStatus()
        );
        assertDoesNotThrow(() -> todosService.getAll(
            BigInteger.valueOf(1500),
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
                BigInteger.valueOf(1500),
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

        verify(todosRepo, times(3)).getUserTodos(
            any(BigInteger.class),
            any(String.class),
            any(String.class),
            any(Integer.class),
            any(Integer.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(Pageable.class)
        );
        verify(userService, times(4)).validateUserToken(
            any(BigInteger.class), any(String.class)
        );
    }

    @Test
    void newTodo() {
        when(todosRepo.save(any(TodoEntity.class))).thenReturn(todo);

        assertEquals(todo.getId(), todosService.newTodo(todoDTO).getId());
        assertDoesNotThrow(() -> todosService.newTodo(todoDTO));

        verify(todosRepo, times(2)).save(any(TodoEntity.class));
    }

    @Test
    void updateTodo() {
        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
            .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(todosRepo.save(any(TodoEntity.class))).thenReturn(todo);

        assertEquals(todo, todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, token));
        assertEquals(
            todo.getDescription(),
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, token)
                .getDescription()
        );
        assertDoesNotThrow(() -> {
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, falseToken);
        });

        verify(userService, times(4)).validateUserToken(any(BigInteger.class), any(String.class));
        verify(todosRepo, times(4)).findById(any(BigInteger.class));
        verify(todosRepo, times(3)).save(any(TodoEntity.class));
    }

    @Test
    void updateStatus() {
        TodosStatusDTO statusDTO = new TodosStatusDTO(Status.DONE);

        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
                .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
                .thenThrow(ForbiddenException.class);
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));
        when(todosRepo.save(any(TodoEntity.class))).thenReturn(todo);

        assertEquals(todo, todosService.updateStatus(BigInteger.valueOf(1500), statusDTO, token));
        assertEquals(
            todo.getStatus(),
            todosService.update(BigInteger.valueOf(1500), todoUpdateDTO, token)
                .getStatus()
        );
        assertDoesNotThrow(() -> {
            todosService.updateStatus(BigInteger.valueOf(1500), statusDTO, token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.updateStatus(BigInteger.valueOf(1000), statusDTO, falseToken);
        });

        verify(userService, times(4))
            .validateUserToken(any(BigInteger.class), any(String.class));
        verify(todosRepo, times(4)).findById(any(BigInteger.class));
        verify(todosRepo, times(3)).save(any(TodoEntity.class));
    }

    @Test
    void deleteTodo() {
        when(userService.validateUserToken(any(BigInteger.class), eq(token)))
                .thenReturn(user);
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
                .thenThrow(ForbiddenException.class);
        when(todosRepo.findById(any(BigInteger.class))).thenReturn(Optional.of(todo));

        assertEquals(todo, todosService.delete(BigInteger.valueOf(1500), token));
        assertEquals(
            todo.getPriority(),
            todosService.delete(BigInteger.valueOf(1500), token)
                .getPriority()
        );
        assertDoesNotThrow(() -> {
            todosService.delete(BigInteger.valueOf(1500), token);
        });
        assertThrows(ForbiddenException.class, () -> {
            todosService.delete(BigInteger.valueOf(1000), falseToken);
        });

        verify(userService, times(4))
            .validateUserToken(any(BigInteger.class), any(String.class));
        verify(todosRepo, times(4)).findById(any(BigInteger.class));
        verify(todosRepo, times(3)).deleteById(any(BigInteger.class));
    }
}
