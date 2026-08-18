package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.LearningAnalyticsService;
import com.mangdehenzhi.service.LearningAnalyticsService.LearningAnalytics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学习分析仪表盘接口
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class LearningAnalyticsController {

    private final LearningAnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<LearningAnalytics>> getDashboard(
            @AuthenticationPrincipal User user) {
        LearningAnalytics analytics = analyticsService.getAnalytics(user.getId());
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }
}