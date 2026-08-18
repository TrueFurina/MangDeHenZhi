package com.mangdehenzhi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 交互式学习服务 — 苏格拉底提问法 + 费曼学习法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningService {

    private final DeepSeekService deepSeekService;
    private final ObjectMapper objectMapper;

    /**
     * 苏格拉底式提问：基于教学内容生成引导性问题
     */
    public List<String> generateSocraticQuestions(String topic, String content) {
        if (deepSeekService.isEnabled()) {
            try {
                String result = deepSeekService.generateSocraticQuestions(topic, content);
                if (result != null) {
                    @SuppressWarnings("unchecked")
                    List<String> questions = objectMapper.readValue(result, List.class);
                    return questions;
                }
            } catch (Exception e) {
                log.warn("AI 苏格拉底提问失败，使用本地模板: {}", e.getMessage());
            }
        }
        return getDefaultQuestions(topic);
    }

    /**
     * 费曼学习法检验：评估用户对某个概念的解释
     */
    public FeynmanFeedback evaluateFeynmanExplanation(String concept, String userExplanation) {
        if (deepSeekService.isEnabled()) {
            try {
                String result = deepSeekService.evaluateFeynmanExplanation(concept, userExplanation);
                if (result != null) {
                    return objectMapper.readValue(result, FeynmanFeedback.class);
                }
            } catch (Exception e) {
                log.warn("AI 费曼评估失败，使用本地评估: {}", e.getMessage());
            }
        }
        return localEvaluate(concept, userExplanation);
    }

    /**
     * 根据用户回答生成后续引导问题（自适应教学）
     */
    public String generateFollowUpQuestion(String topic, String previousAnswer) {
        if (deepSeekService.isEnabled()) {
            try {
                return deepSeekService.generateFollowUpQuestion(topic, previousAnswer);
            } catch (Exception e) {
                log.warn("AI 追问生成失败: {}", e.getMessage());
            }
        }
        return "你能进一步解释一下这个概念吗？最好能用一个生活中的例子来说明。";
    }

    private List<String> getDefaultQuestions(String topic) {
        return List.of(
            "关于「" + topic + "」，你能用自己的话解释一下它是什么吗？",
            "你在实际生活中遇到过与「" + topic + "」相关的例子吗？",
            "你觉得「" + topic + "」和你之前学过的知识有什么联系？",
            "如果让你向一个完全不懂的人解释「" + topic + "」，你会怎么说？",
            "「" + topic + "」有哪些局限性或需要注意的地方？"
        );
    }

    private FeynmanFeedback localEvaluate(String concept, String explanation) {
        int length = explanation.length();
        boolean hasExample = explanation.contains("比如") || explanation.contains("例如") || explanation.contains("就像");
        int score = Math.min(100, 30 + length / 5 + (hasExample ? 20 : 0));

        List<String> suggestions = new ArrayList<>();
        if (length < 50) {
            suggestions.add("你的解释比较简短，试着用更详细的描述来说明");
        }
        if (!hasExample) {
            suggestions.add("尝试用一个具体的例子来解释，这样更容易理解");
        }
        suggestions.add("试试用更简单的语言，想象你在向一个初学者解释这个概念");

        return new FeynmanFeedback(score, "你的解释" + (score >= 70 ? "很不错" : "还有提升空间"), suggestions);
    }

    public record FeynmanFeedback(int score, String summary, List<String> suggestions) {}
}