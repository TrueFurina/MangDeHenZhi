package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.AiChatRequest;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.DeepSeekService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final DeepSeekService deepSeekService;

    private static final String SYSTEM_INSTRUCTION =
            "你是一个专业的AI学习导师，擅长用苏格拉底提问法和费曼学习法帮助学生理解知识。" +
            "请用中文回答，鼓励学生思考，而不是直接给出答案。";

    /**
     * AI 导师对话端点。
     * 要求认证（@AuthenticationPrincipal），内部复用 DeepSeekService 调用大模型。
     * system 与 user 消息分离传递，避免提示词注入；输出做基本校验。
     */
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<String>> chat(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AiChatRequest request) {

        List<Map<String, String>> conversation = request.getMessages().stream()
                .map(m -> Map.of("role", m.getRole(), "content", m.getContent()))
                .toList();

        String reply = deepSeekService.chat(SYSTEM_INSTRUCTION, conversation);

        if (reply == null) {
            // DeepSeek 未配置或调用失败：返回 503，由前端降级处理
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error(503, 1000, "AI 导师服务暂时不可用，请稍后再试"));
        }

        return ResponseEntity.ok(ApiResponse.success(reply));
    }

    /**
     * 面试/答题提示端点 — 卡壳时给出引导性提示
     */
    @PostMapping("/hint")
    public ResponseEntity<ApiResponse<String>> getHint(
            @AuthenticationPrincipal User user,
            @RequestBody HintRequest request) {

        String hint = deepSeekService.generateInterviewHint(request.question(), request.dimension());

        if (hint == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error(503, 1000, "AI 提示服务暂时不可用，请稍后再试"));
        }

        return ResponseEntity.ok(ApiResponse.success(hint));
    }

    public record HintRequest(String question, String dimension) {}
}
