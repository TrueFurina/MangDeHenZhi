package com.mangdehenzhi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * AI分析服务 — 优先使用 DeepSeek 大模型 API，不可用时降级为模拟分析
 */
@Slf4j
@Service
public class AIService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final DeepSeekService deepSeekService;

    public AIService(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    /**
     * 分析测评结果，生成AI分析报告
     * 优先调用 DeepSeek API，失败时使用本地模拟
     */
    public String analyzeAssessmentResult(String title, Map<String, Integer> dimensionScores, int totalScore) {
        // 尝试 DeepSeek API
        if (deepSeekService.isEnabled()) {
            try {
                String result = deepSeekService.analyzeAssessmentResult(title, dimensionScores, totalScore);
                if (result != null) {
                    log.info("使用 DeepSeek API 生成分析报告");
                    return result;
                }
            } catch (Exception e) {
                log.warn("DeepSeek API 分析失败，降级到本地模拟: {}", e.getMessage());
            }
        }

        // 降级：本地模拟分析
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("overallAssessment", generateOverallAssessment(totalScore));
        analysis.put("strengthDimensions", findStrengthDimensions(dimensionScores));
        analysis.put("weaknessDimensions", findWeaknessDimensions(dimensionScores));
        analysis.put("improvementSuggestions", generateImprovements(dimensionScores));
        analysis.put("aiConfidence", 0.85);

        return toJson(analysis);
    }

    /**
     * 根据测评结果生成个性化学习推荐
     */
    public String generateRecommendations(String title, Map<String, Integer> dimensionScores) {
        // 尝试 DeepSeek API
        if (deepSeekService.isEnabled()) {
            try {
                String result = deepSeekService.generateRecommendations(title, dimensionScores);
                if (result != null) {
                    log.info("使用 DeepSeek API 生成学习推荐");
                    return result;
                }
            } catch (Exception e) {
                log.warn("DeepSeek API 推荐失败，降级到本地模拟: {}", e.getMessage());
            }
        }

        // 降级：本地模拟推荐
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("recommendedCourses", generateRecommendedCourses(dimensionScores));
        recommendations.put("learningPath", generateLearningPath(dimensionScores));
        recommendations.put("estimatedImprovementTime", "2-4周");
        recommendations.put("practiceSuggestions", generatePracticeSuggestions(dimensionScores));

        return toJson(recommendations);
    }

    // ========== 本地模拟逻辑（降级方案） ==========

    private String generateOverallAssessment(int totalScore) {
        if (totalScore >= 90) return "优秀：表现出色，具备扎实的技能基础";
        if (totalScore >= 75) return "良好：整体能力不错，仍有提升空间";
        if (totalScore >= 60) return "及格：基础能力达标，建议加强薄弱环节";
        return "需要提升：建议重新学习基础课程后再次测评";
    }

    private Map<String, Integer> findStrengthDimensions(Map<String, Integer> scores) {
        Map<String, Integer> strengths = new HashMap<>();
        scores.forEach((dim, score) -> {
            if (score >= 80) strengths.put(dim, score);
        });
        return strengths;
    }

    private Map<String, Integer> findWeaknessDimensions(Map<String, Integer> scores) {
        Map<String, Integer> weaknesses = new HashMap<>();
        scores.forEach((dim, score) -> {
            if (score < 60) weaknesses.put(dim, score);
        });
        return weaknesses;
    }

    private Map<String, String> generateImprovements(Map<String, Integer> scores) {
        Map<String, String> improvements = new HashMap<>();
        scores.forEach((dim, score) -> {
            if (score < 60) {
                improvements.put(dim, "建议重新学习" + dim + "相关课程，加强基础练习");
            } else if (score < 80) {
                improvements.put(dim, "可以通过进阶练习进一步提升" + dim + "能力");
            } else {
                improvements.put(dim, "保持当前水平，尝试挑战更高难度");
            }
        });
        return improvements;
    }

    private String[] generateRecommendedCourses(Map<String, Integer> scores) {
        return new String[]{
            "沟通技巧进阶课程",
            "团队协作实战训练",
            "问题解决方法论"
        };
    }

    private String[] generateLearningPath(Map<String, Integer> scores) {
        return new String[]{
            "第一阶段：基础巩固（1周）",
            "第二阶段：专项提升（1-2周）",
            "第三阶段：实战应用（1周）"
        };
    }

    private String[] generatePracticeSuggestions(Map<String, Integer> scores) {
        return new String[]{
            "每周至少完成3次模拟练习",
            "参与线上讨论与协作项目",
            "定期复盘学习进度"
        };
    }

    private String toJson(Map<String, Object> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            return "{}";
        }
    }
}