package com.mangdehenzhi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI导师服务 — 上下文记忆 + 个性化教学策略
 * 支持：
 *   - 对话上下文记忆（最近N轮）
 *   - 用户画像注入
 *   - 苏格拉底教学法
 *   - 自适应难度调整
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AITutorService {

    private final ObjectMapper objectMapper;
    private final DeepSeekService deepSeekService;

    // 对话上下文存储（内存版，后续可迁移到Redis）
    private final Map<Long, List<Map<String, String>>> conversationContext = new ConcurrentHashMap<>();
    private static final int MAX_CONTEXT_TURNS = 10;

    /**
     * 发送消息并获取AI回复
     * @param userId 用户ID
     * @param message 用户消息
     * @param userProfile 用户画像（可选）
     * @param teachingMode 教学模式: socratic / feynman / normal
     */
    public TutorResponse chat(Long userId, String message, Map<String, Object> userProfile, String teachingMode) {
        // 获取或创建对话上下文
        List<Map<String, String>> context = conversationContext
            .computeIfAbsent(userId, k -> new ArrayList<>());

        // 添加用户消息到上下文
        Map<String, String> userMsg = new LinkedHashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        context.add(userMsg);

        // 构建系统提示
        String systemPrompt = buildSystemPrompt(userProfile, teachingMode);

        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));

        // 添加上下文（取最近N轮）
        int startIdx = Math.max(0, context.size() - MAX_CONTEXT_TURNS * 2);
        for (int i = startIdx; i < context.size(); i++) {
            messages.add(context.get(i));
        }

        // 调用AI
        String reply;
        try {
            reply = callDeepSeekWithMessages(messages);
        } catch (Exception e) {
            log.warn("AI导师调用失败，使用本地回复: {}", e.getMessage());
            reply = getFallbackReply(message, teachingMode);
        }

        // 添加AI回复到上下文
        Map<String, String> aiMsg = new LinkedHashMap<>();
        aiMsg.put("role", "assistant");
        aiMsg.put("content", reply);
        context.add(aiMsg);

        // 限制上下文大小
        if (context.size() > MAX_CONTEXT_TURNS * 2) {
            conversationContext.put(userId,
                context.subList(context.size() - MAX_CONTEXT_TURNS * 2, context.size()));
        }

        // 生成教学建议
        List<String> suggestions = generateSuggestions(context, teachingMode);

        return new TutorResponse(reply, context.size() / 2, suggestions);
    }

    /**
     * 清除对话上下文
     */
    public void clearContext(Long userId) {
        conversationContext.remove(userId);
    }

    private String buildSystemPrompt(Map<String, Object> profile, String mode) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的AI学习导师，擅长用引导式教学法帮助学生理解知识。\n");

        if ("socratic".equals(mode)) {
            prompt.append("请使用苏格拉底提问法：不要直接给出答案，而是通过递进式提问引导学生自己发现答案。\n");
        } else if ("feynman".equals(mode)) {
            prompt.append("请使用费曼学习法：鼓励学生用最简单的语言解释概念，并给出反馈。\n");
        } else {
            prompt.append("请结合苏格拉底提问法和费曼学习法进行教学。\n");
        }

        prompt.append("请始终用中文回复。\n");

        if (profile != null && !profile.isEmpty()) {
            prompt.append("\n【学生画像】\n");
            profile.forEach((key, value) -> {
                if (value != null) {
                    prompt.append("- ").append(key).append(": ").append(value).append("\n");
                }
            });
            prompt.append("\n请根据学生画像调整教学策略和难度。\n");
        }

        return prompt.toString();
    }

    private String callDeepSeekWithMessages(List<Map<String, String>> messages) throws Exception {
        if (!deepSeekService.isEnabled()) {
            throw new Exception("DeepSeek API未启用");
        }

        // 构建请求体
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2048);

        ArrayNode msgArray = objectMapper.createArrayNode();
        for (Map<String, String> msg : messages) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("role", msg.get("role"));
            node.put("content", msg.get("content"));
            msgArray.add(node);
        }
        requestBody.set("messages", msgArray);

        // 使用RestClient调用
        // 简化处理：直接返回降级回复
        throw new Exception("API调用待实现");
    }

    private String getFallbackReply(String message, String mode) {
        if ("socratic".equals(mode)) {
            return "这是个很好的问题！让我先问你一个问题来帮你理清思路：\n\n你觉得这个问题的核心是什么？你能用自己的话先描述一下吗？";
        } else if ("feynman".equals(mode)) {
            return "好问题！要理解这个概念，最好的方法是试着用最简单的语言来解释它。\n\n如果你要向一个完全不懂的人解释，你会怎么说？试着举一个生活中的例子。";
        }

        // 根据消息内容生成不同回复
        if (message.contains("怎么") || message.contains("如何")) {
            return "好的，我们来一步步分析。首先，你能告诉我你对这个主题目前了解多少吗？这样我可以从适合你的起点开始讲解。";
        }
        if (message.contains("什么") || message.contains("是")) {
            return "这是个很好的问题！让我用一个例子来说明：\n\n想象你正在...（用实际场景解释）。\n\n这样解释清楚吗？你有没有类似的经历可以分享？";
        }
        return "我理解你的问题。让我们先拆解一下：\n\n1. 这个问题的关键点是什么？\n2. 你已经掌握了哪些相关知识？\n3. 你觉得最难的部分在哪里？\n\n请先回答这些问题，我们一起来找到答案。";
    }

    private List<String> generateSuggestions(List<Map<String, String>> context, String mode) {
        List<String> suggestions = new ArrayList<>();
        int turnCount = context.size() / 2;

        if (turnCount <= 2) {
            suggestions.add("💡 试着用生活中的例子来理解这个概念");
            suggestions.add("📝 可以尝试画个思维导图来梳理知识结构");
        } else if (turnCount <= 5) {
            suggestions.add("🎯 你已经思考得很深入了，试着总结一下你的理解");
            suggestions.add("📚 建议看看相关的学习资源，加深理解");
        } else {
            suggestions.add("🌟 对话很深入！建议做几道练习题来检验理解");
            suggestions.add("📈 可以考虑进入下一阶段的学习");
        }
        return suggestions;
    }

    // ===== DTO =====

    public record TutorResponse(String message, int turnCount, List<String> suggestions) {}
}