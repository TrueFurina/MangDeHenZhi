package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.repository.AssessmentResultRepository;
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
    private final AssessmentResultRepository assessmentResultRepository;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<Course>>> recommendCourses(
            @AuthenticationPrincipal User user) {
        // 获取用户最新测评结果进行推荐
        List<AssessmentResult> results = assessmentResultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(List.of()));
        }
        AssessmentResult latest = results.get(results.size() - 1);
        List<Course> recommended = recommendationService.recommendCourses(latest);
        return ResponseEntity.ok(ApiResponse.success(recommended));
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