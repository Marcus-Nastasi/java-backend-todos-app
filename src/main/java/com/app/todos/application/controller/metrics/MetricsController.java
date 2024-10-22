package com.app.todos.application.controller.metrics;

import com.app.todos.application.service.metrics.MetricService;
import com.app.todos.domain.metrics.dtos.MetricsQntByPriorDto;
import com.app.todos.domain.metrics.dtos.MetricsQntByStatResponseDto;
import com.app.todos.domain.todos.Todo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/metrics")
@SecurityRequirement(name = "Bearer Authentication")
public class MetricsController {

    @Autowired
    private MetricService metricService;

    @GetMapping("/{user_id}")
    public ResponseEntity<MetricsQntByStatResponseDto> get(
            @PathVariable("user_id") BigInteger user_id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  to,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity
            .ok(metricService.getTodosByUser(user_id, token, from, to));
    }

    @GetMapping(value = "/all/{user_id}")
    public ResponseEntity<List<Todo>> all(
            @PathVariable BigInteger user_id,
            @RequestParam("query") @Nullable String query,
            @RequestParam("status") @Nullable String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  to,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        List<Todo> todoList = metricService.get(
            user_id,
            token,
            query,
            status,
            from,
            to
        );
        return ResponseEntity.ok(todoList);
    }
}
