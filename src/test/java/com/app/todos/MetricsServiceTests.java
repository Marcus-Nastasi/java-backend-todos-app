package com.app.todos;

import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.metrics.MetricService;
import com.app.todos.application.service.todos.TodosService;
import com.app.todos.application.service.users.UserService;
import com.app.todos.domain.metrics.dtos.MetricsNumbersResponseDto;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.users.User;
import com.app.todos.resources.enums.todos.Priority;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MetricsNumbersResponseDto metricsQntByPriorDto = new MetricsNumbersResponseDto(
            1L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, BigDecimal.valueOf(0)
        );

        when(userService.validateUserToken(any(BigInteger.class), any(String.class))).thenReturn(user);
        when(todosRepo.metricsQuery(
            BigInteger.valueOf(1), "query", LocalDate.of(2024, 1, 15), LocalDate.now()
        )).thenReturn(Map.of(
            "total", BigDecimal.valueOf(1),
            "high", BigDecimal.valueOf(1),
            "medium", BigDecimal.valueOf(0),
            "low", BigDecimal.valueOf(0),
            "pending", BigDecimal.valueOf(1),
            "inprogress", BigDecimal.valueOf(0),
            "done", BigDecimal.valueOf(0),
            "overdue", BigDecimal.valueOf(0),
            "future", BigDecimal.valueOf(0),
            "completion_rate", BigDecimal.valueOf(0)
        ));
        when(todosRepo.metricsQuery(
            BigInteger.valueOf(1), "query", LocalDate.of(2024, 7, 10), LocalDate.now()
        )).thenReturn(Map.of(
            "total", BigDecimal.valueOf(1),
            "high", BigDecimal.valueOf(1),
            "medium", BigDecimal.valueOf(0),
            "low", BigDecimal.valueOf(0),
            "pending", BigDecimal.valueOf(1),
            "inprogress", BigDecimal.valueOf(0),
            "done", BigDecimal.valueOf(0),
            "overdue", BigDecimal.valueOf(0),
            "future", BigDecimal.valueOf(0),
            "completion_rate", BigDecimal.valueOf(0)
        ));

        assertEquals(
            metricsQntByPriorDto,
            metricService.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            )
        );
        assertEquals(
            metricsQntByPriorDto,
            metricService.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );

        verify(todosRepo, times(2)).metricsQuery(
            any(BigInteger.class),
            any(String.class),
            any(LocalDate.class),
            any(LocalDate.class)
        );
    }
}
