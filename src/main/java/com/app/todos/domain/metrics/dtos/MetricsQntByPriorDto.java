package com.app.todos.domain.metrics.dtos;

public record MetricsQntByPriorDto(
        Long high,
        Long medium,
        Long low
) {}
