package com.mangdehenzhi.ai;

import lombok.Data;
import java.util.Map;

/**
 * AI测评分析结果模型
 */
@Data
public class AnalysisReport {
    private String overallAssessment;       // 总体评价
    private Map<String, Integer> strengths;  // 优势维度
    private Map<String, Integer> weaknesses; // 薄弱维度
    private Map<String, String> improvements; // 改进建议
    private double confidence;               // AI置信度
}