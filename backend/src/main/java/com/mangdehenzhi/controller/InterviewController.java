package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * AI 模拟面试 — 多智能体面试官（借鉴 ai_interview）
 * 生成面试题 + 评估回答
 */
@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final DeepSeekService deepSeekService;

    /** 生成模拟面试题 */
    @PostMapping("/questions")
    public ResponseEntity<ApiResponse<String>> generateQuestions(
            @AuthenticationPrincipal User user,
            @RequestBody QuestionsRequest request) {
        String result = deepSeekService.generateInterviewQuestions(
                request.jobTitle(), request.dimension(), request.count() > 0 ? request.count() : 3);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error(503, "AI 面试官服务暂时不可用"));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /** 评估面试回答 */
    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<String>> evaluate(
            @AuthenticationPrincipal User user,
            @RequestBody EvaluateRequest request) {
        String result = deepSeekService.evaluateInterviewAnswer(request.question(), request.answer());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error(503, "AI 评分官服务暂时不可用"));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    public record QuestionsRequest(String jobTitle, String dimension, int count) {}
    public record EvaluateRequest(String question, String answer) {}
}
