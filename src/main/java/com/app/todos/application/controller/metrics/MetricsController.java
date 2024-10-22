package com.app.todos.application.controller.metrics;

import com.app.todos.application.service.metrics.MetricService;
import com.app.todos.domain.metrics.dtos.MetricsQntByPriorDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/metrics")
@SecurityRequirement(name = "Bearer Authentication")
public class MetricsController {

    @Autowired
    private MetricService metricService;

    @GetMapping("/{user_id}")
    public ResponseEntity<MetricsQntByPriorDto> get(
            @PathVariable("user_id") BigInteger user_id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  to,
            @RequestHeader Map<String, String> headers
    ) {
        String token = headers.get("authorization").replace("Bearer ", "");
        return ResponseEntity
            .ok(metricService.getTodosQuantityByPriority(user_id, token));
    }
}
