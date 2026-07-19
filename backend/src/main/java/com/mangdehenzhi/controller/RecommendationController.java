package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.AssessmentResultDTO;
import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.AssessmentService;
import com.mangdehenzhi.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final AssessmentService assessmentService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<Course>>> recommendCourses(
            @AuthenticationPrincipal User user) {
        var results = assessmentService.getUserResults(user.getId());
        if (results.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(recommendationService.getDefaultCourses()));
        }
        var latest = results.get(results.size() - 1);
        var assessmentResult = assessmentService.getResultById(latest.getId(), user.getId());
        var courses = recommendationService.recommendFromDimensions(assessmentResult);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/learning-path")
    public ResponseEntity<ApiResponse<List<String>>> getLearningPath(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(List.of(
            "📚 基础巩固阶段 - 针对薄弱环节系统学习",
            "🚀 能力提升阶段 - 进阶技能训练",
            "🎯 实战应用阶段 - 项目实战与模拟",
            "🏆 认证冲刺阶段 - 准备最终测评与认证"
        )));
    }
}