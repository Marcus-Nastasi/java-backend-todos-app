package com.app.todos;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.metrics.MetricUseCase;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.adapters.output.metrics.MetricsNumbersResponseDto;
import com.app.todos.domain.users.User;
import com.app.todos.application.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricsServiceTests {

    @Mock
    private TodosGateway todosGateway;
    @Mock
    private UserUseCase userUseCase;
    @InjectMocks
    private MetricUseCase metricUseCase;

    User user = new User(BigInteger.valueOf(1), "Brian", "bian@gmail.com", "12345");
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodosByUserTests() {
        MetricsNumbersResponseDto metricsQntByPriorDto = new MetricsNumbersResponseDto(
            1L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, BigDecimal.valueOf(0.77)
        );

        when(userUseCase.validateUserToken(any(BigInteger.class), any(String.class))).thenReturn(user);
        when(todosGateway.getMetrics(BigInteger.valueOf(1), "query", LocalDate.of(2024, 1, 15), LocalDate.now()))
            .thenReturn(Map.of(
                "total", BigDecimal.valueOf(1),
                "high", BigDecimal.valueOf(1),
                "medium", BigDecimal.valueOf(0),
                "low", BigDecimal.valueOf(0),
                "pending", BigDecimal.valueOf(1),
                "inprogress", BigDecimal.valueOf(0),
                "done", BigDecimal.valueOf(0),
                "overdue", BigDecimal.valueOf(0),
                "future", BigDecimal.valueOf(0),
                "completion_rate", BigDecimal.valueOf(0.77)
            ));
        when(todosGateway.getMetrics(
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
        when(userUseCase.validateUserToken(any(BigInteger.class), eq(falseToken))).thenThrow(ForbiddenException.class);

        assertEquals(
            metricsQntByPriorDto.completion_rate(),
            metricUseCase.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            ).getCompletion_rate()
        );
        assertEquals(
            metricsQntByPriorDto.pending(),
            metricUseCase.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            ).getPending()
        );
        assertDoesNotThrow(
            () -> metricUseCase.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );
        assertThrows(
            ForbiddenException.class,
            () -> metricUseCase.get(
                BigInteger.valueOf(1),
                falseToken,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );

        verify(todosGateway, times(3)).getMetrics(
            any(BigInteger.class),
            any(String.class),
            any(LocalDate.class),
            any(LocalDate.class)
        );
    }
}
