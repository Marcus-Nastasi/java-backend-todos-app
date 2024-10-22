package com.app.todos;

import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.metrics.MetricService;
import com.app.todos.application.service.todos.TodosService;
import com.app.todos.application.service.users.UserService;
import com.app.todos.domain.todos.DTOs.TodosPageResponseDto;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.users.User;
import com.app.todos.resources.enums.todos.Priority;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MetricsServiceTests {

    @Mock
    private TodosRepo todosRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private TokenService tokenService;
    @Mock
    private TodosService todosService;
    @Mock
    private UserService userService;
    @InjectMocks
    private MetricService metricService;

    Todo todo = new Todo(BigInteger.valueOf(1500), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 2, 15), Priority.HIGH);
    Todo todo2 = new Todo(BigInteger.valueOf(1600), "Coca-Cola", "Make machine", "Make refri machine", "none", LocalDate.of(2024, 7, 15), Priority.LOW);
    User user = new User("Brian", "bian@gmail.com", "12345");
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodosByUserTests() {
        when(
            todosService.getAllNoPag(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            )
        ).thenReturn(List.of(todo, todo2));
        when(
            todosService.getAllNoPag(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        ).thenReturn(List.of(todo2));

        assertEquals(
            2,
            metricService.getTodosByUser(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            )
        );
        assertEquals(
            1,
            metricService.getTodosByUser(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );

        verify(
            todosService,
            times(2)
        ).getAllNoPag(any(BigInteger.class), any(String.class), any(LocalDate.class), any(LocalDate.class));
    }
}
