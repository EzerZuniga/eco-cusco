package com.cusco.limpio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;

    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        boolean dbUp = false;
        try {
            Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            dbUp = (one != null && one == 1);
        } catch (Exception ex) {
            dbUp = false;
        }

        Map<String, Object> body = Map.of(
                "status", dbUp ? "UP" : "DEGRADED",
                "database", dbUp,
                "timestamp", Instant.now().toString()
        );

        return ResponseEntity.ok(body);
    }
}
