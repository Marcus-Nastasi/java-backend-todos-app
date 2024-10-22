package com.app.todos.domain.metrics.dtos;

import java.math.BigDecimal;

public record MetricsNumbersResponseDto(

    Long total,
    Long high,
    Long medium,
    Long low,
    Long pending,
    Long in_progress,
    Long done,
    Long overdue,
    Long future,
    BigDecimal completion_rate
) {}
