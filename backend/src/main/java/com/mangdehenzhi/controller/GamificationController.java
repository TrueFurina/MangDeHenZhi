package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    /** 获取我的游戏化概览（XP/等级/成就） */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMySummary(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(
                gamificationService.getGamificationSummary(user.getId())));
    }

    /** 手动触发 XP 事件（如 LESSON_COMPLETE / AI_CHAT） */
    @PostMapping("/xp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addXp(
            @AuthenticationPrincipal User user,
            @RequestBody XpRequest request) {
        gamificationService.addXp(user.getId(), request.eventType());
        return ResponseEntity.ok(ApiResponse.success(
                gamificationService.getGamificationSummary(user.getId())));
    }

    public record XpRequest(String eventType) {}
}
