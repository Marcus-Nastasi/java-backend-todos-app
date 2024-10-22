package com.app.todos.domain.metrics.dtos;

public record MetricsQntByStatResponseDto(
    Long pending,
    Long done,
    Long inProgress,
    Long total
) {}
