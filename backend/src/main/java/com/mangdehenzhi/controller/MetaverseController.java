package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.CreateMetaverseSessionRequest;
import com.mangdehenzhi.entity.MetaverseSession;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.MetaverseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metaverse")
@RequiredArgsConstructor
public class MetaverseController {

    private final MetaverseService metaverseService;

    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<MetaverseSession>> createSession(
            @AuthenticationPrincipal User user,
            @RequestBody CreateMetaverseSessionRequest request) {
        MetaverseSession session = metaverseService.createSession(
                user.getId(), request.getSessionName(),
                request.getSceneType(), request.getSceneConfig());
        return ResponseEntity.ok(ApiResponse.success("场景创建成功", session));
    }

    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<MetaverseSession>>> getMySessions(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(metaverseService.getUserSessions(user.getId())));
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<ApiResponse<MetaverseSession>> getSession(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        MetaverseSession session = metaverseService.getSessionById(id);
        if (!session.getUser().getId().equals(user.getId())) {
            throw new com.mangdehenzhi.exception.BusinessException(403, "无权访问此会话");
        }
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @PostMapping("/sessions/{id}/end")
    public ResponseEntity<ApiResponse<MetaverseSession>> endSession(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        MetaverseSession session = metaverseService.getSessionById(id);
        if (!session.getUser().getId().equals(user.getId())) {
            throw new com.mangdehenzhi.exception.BusinessException(403, "无权结束此会话");
        }
        return ResponseEntity.ok(ApiResponse.success(metaverseService.endSession(id)));
    }

    @GetMapping("/scene-config/{sceneType}")
    public ResponseEntity<ApiResponse<String>> getSceneConfig(@PathVariable String sceneType) {
        try {
            com.mangdehenzhi.enums.MetaverseSceneType type =
                    com.mangdehenzhi.enums.MetaverseSceneType.valueOf(sceneType.toUpperCase());
            return ResponseEntity.ok(ApiResponse.success(metaverseService.getSceneConfig(type)));
        } catch (IllegalArgumentException e) {
            throw new com.mangdehenzhi.exception.BusinessException("无效的场景类型: " + sceneType);
        }
    }
}