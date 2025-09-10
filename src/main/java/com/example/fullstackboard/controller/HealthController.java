package com.example.fullstackboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/health/db")
    public Map<String, String> healthDb() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Map.of("status", "UP");
        } catch (Exception e) {
            return Map.of("status", "DOWN");
        }
    }
}
