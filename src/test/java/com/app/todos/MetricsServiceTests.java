package com.app.todos;

import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.metrics.MetricService;
import com.app.todos.application.service.todos.TodosService;
import com.app.todos.application.service.users.UserService;
import com.app.todos.domain.metrics.dtos.MetricsQntByPriorDto;
import com.app.todos.domain.metrics.dtos.MetricsQntByStatResponseDto;
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
import java.util.Map;
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
        MetricsQntByStatResponseDto metricsQntByPriorDto = new MetricsQntByStatResponseDto(
            1L, 1L, 1L, 3L
        );
        MetricsQntByStatResponseDto metricsQntByPriorDto2 = new MetricsQntByStatResponseDto(
            1L, 0L, 1L, 2L
        );

        when(
            todosRepo.findQuantityByStatus(
                BigInteger.valueOf(1),
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            )
        ).thenReturn(Map.of("pending", 1L, "inProgress", 1L, "done", 1L, "total", 3L));
        when(
            todosRepo.findQuantityByStatus(
                BigInteger.valueOf(1),
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        ).thenReturn(Map.of("pending", 1L, "inProgress", 0L, "done", 1L, "total", 2L));

        assertEquals(
            metricsQntByPriorDto,
            metricService.getTodosByUser(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            )
        );
        assertEquals(
            metricsQntByPriorDto2,
            metricService.getTodosByUser(
                BigInteger.valueOf(1),
                token,
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );

        verify(
            todosRepo,
            times(2)
        ).findQuantityByStatus(any(BigInteger.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getTodosByPriority() {
        MetricsQntByPriorDto metricsQntByPriorDto = new MetricsQntByPriorDto(1L, 1L, 1L);
        when(todosRepo.findQuantityByPriority(any(BigInteger.class)))
            .thenReturn(Map.of("high", 1L, "medium", 1L, "low", 1L));

        assertEquals(
            metricsQntByPriorDto,
            metricService.getTodosQuantityByPriority(BigInteger.valueOf(1), token)
        );

        verify(
            todosRepo,
            times(1)
        ).findQuantityByPriority(any(BigInteger.class));
    }
}
