package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 健康检查端点 — 用于 Docker 容器探活和监控
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "version", "1.0.0",
                "service", "mangdehenzhi-backend"
        )));
    }
}