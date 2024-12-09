package com.app.todos;

import com.app.todos.application.usecases.metrics.MetricService;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.adapters.output.metrics.MetricsNumbersResponseDto;
import com.app.todos.infrastructure.entity.users.UserEntity;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
import com.app.todos.application.exception.ForbiddenException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MetricsServiceTests {

    @Mock
    private TodosRepo todosRepo;
    @Mock
    private UserUseCase userService;
    @InjectMocks
    private MetricService metricService;

    UserEntity user = new UserEntity("Brian", "bian@gmail.com", "12345");
    String token = "Token";
    String falseToken = "Token False";

    @Test
    void getTodosByUserTests() {
        MetricsNumbersResponseDto metricsQntByPriorDto = new MetricsNumbersResponseDto(
            1L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, BigDecimal.valueOf(0.77)
        );

        when(userService.validateUserToken(any(BigInteger.class), any(String.class)))
            .thenReturn(user);
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
            "completion_rate", BigDecimal.valueOf(0.77)
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
        when(userService.validateUserToken(any(BigInteger.class), eq(falseToken)))
            .thenThrow(ForbiddenException.class);

        assertEquals(
            metricsQntByPriorDto.completion_rate(),
            metricService.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 1, 15),
                LocalDate.now()
            ).completion_rate()
        );
        assertEquals(
            metricsQntByPriorDto.pending(),
            metricService.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            ).pending()
        );
        assertDoesNotThrow(
            () -> metricService.get(
                BigInteger.valueOf(1),
                token,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );
        assertThrows(
            ForbiddenException.class,
            () -> metricService.get(
                BigInteger.valueOf(1),
                falseToken,
                "query",
                LocalDate.of(2024, 7, 10),
                LocalDate.now()
            )
        );

        verify(todosRepo, times(3)).metricsQuery(
            any(BigInteger.class),
            any(String.class),
            any(LocalDate.class),
            any(LocalDate.class)
        );
    }
}
