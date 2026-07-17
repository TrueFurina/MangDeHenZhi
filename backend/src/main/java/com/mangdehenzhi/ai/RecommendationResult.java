package com.mangdehenzhi.ai;

import lombok.Data;
import java.util.List;

/**
 * AI推荐结果模型
 */
@Data
public class RecommendationResult {
    private List<String> recommendedCourses;     // 推荐课程
    private List<String> learningPath;            // 学习路径
    private String estimatedImprovementTime;      // 预计提升时间
    private List<String> practiceSuggestions;     // 练习建议
}