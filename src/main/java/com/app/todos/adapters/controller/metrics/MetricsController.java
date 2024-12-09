package com.app.todos.adapters.controller.metrics;

import com.app.todos.application.usecases.metrics.MetricUseCase;
import com.app.todos.adapters.output.metrics.MetricsNumbersResponseDto;
import com.app.todos.domain.metrics.MetricsResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/metrics")
@SecurityRequirement(name = "Bearer Authentication")
public class MetricsController {

    @Autowired
    private MetricUseCase metricService;

    @GetMapping(value = "/all/{user_id}")
    public ResponseEntity<MetricsResponse> all(
            @PathVariable BigInteger user_id,
            @RequestParam("client") @Nullable String client,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  to,
            @RequestHeader Map<String, String> headers
    ) {
        MetricsResponse todoList = metricService.get(
            user_id,
            headers.get("authorization").replace("Bearer ", ""),
            client,
            from,
            to
        );
        return ResponseEntity.ok(todoList);
    }
}
