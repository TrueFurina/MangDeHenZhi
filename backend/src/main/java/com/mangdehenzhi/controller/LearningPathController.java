package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.LearningPathService;
import com.mangdehenzhi.service.LearningPathService.LearningPathResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 个性化学习路径推荐接口
 */
@RestController
@RequestMapping("/api/learning-path")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<LearningPathResult>> recommend(
            @AuthenticationPrincipal User user) {
        Map<String, Integer> scores = learningPathService.getLatestAssessmentScores(user.getId());
        LearningPathResult result = learningPathService.generateLearningPath(scores, 5);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<LearningPathResult>> analyze(
            @RequestBody Map<String, Integer> skillScores) {
        LearningPathResult result = learningPathService.generateLearningPath(skillScores, 5);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}