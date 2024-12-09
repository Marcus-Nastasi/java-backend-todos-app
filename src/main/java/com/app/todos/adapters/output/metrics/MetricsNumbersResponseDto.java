package com.app.todos.adapters.output.metrics;

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
