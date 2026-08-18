package com.mangdehenzhi.recruitment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangdehenzhi.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 网申填报助手 — AI 辅助填写申请表单
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationAssistantService {

    private final DeepSeekService deepSeekService;
    private final ObjectMapper objectMapper;

    /**
     * 生成网申表单的 AI 填报建议
     *
     * @param companyName  公司名称
     * @param positionName 职位名称
     * @param jobDesc      职位描述
     * @param userSkills   用户技能
     * @param formFields   表单字段列表
     * @return 各字段的 AI 建议内容
     */
    public Map<String, String> generateSuggestions(
            String companyName,
            String positionName,
            String jobDesc,
            Map<String, Integer> userSkills,
            List<String> formFields) {

        Map<String, String> suggestions = new LinkedHashMap<>();

        // 尝试 DeepSeek AI 生成
        if (deepSeekService.isEnabled()) {
            try {
                String aiResult = deepSeekService.generateApplicationSuggestions(
                        companyName, positionName, jobDesc, userSkills, formFields);
                if (aiResult != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> parsed = objectMapper.readValue(aiResult, Map.class);
                    suggestions.putAll(parsed);
                    log.info("AI 网申建议生成成功: {} 字段", parsed.size());
                    return suggestions;
                }
            } catch (Exception e) {
                log.warn("AI 网申建议生成失败，使用本地模板: {}", e.getMessage());
            }
        }

        // 本地模板生成
        for (String field : formFields) {
            suggestions.put(field, generateLocalSuggestion(field, companyName, positionName, userSkills));
        }
        return suggestions;
    }

    /**
     * 分析简历与职位的匹配度，给出优化建议
     */
    public String analyzeResumeFit(String resumeText, String jobDescription) {
        if (deepSeekService.isEnabled()) {
            try {
                return deepSeekService.analyzeResumeFit(resumeText, jobDescription);
            } catch (Exception e) {
                log.warn("AI 简历分析失败: {}", e.getMessage());
            }
        }
        return "💡 建议：突出与职位描述相关的技能和项目经验，使用量化成果展示你的能力。";
    }

    private String generateLocalSuggestion(String field, String company, String position, Map<String, Integer> skills) {
        return switch (field.toLowerCase()) {
            case "自我介绍", "self_intro" -> String.format(
                    "我是对%s职位有浓厚兴趣的应届生，具备%s等技能，希望能为%s贡献价值。",
                    position, formatSkills(skills), company);
            case "优势", "strengths" -> String.format(
                    "核心优势：%s。在校期间积累了扎实的专业基础和项目经验。",
                    formatSkills(skills));
            case "职业规划", "career_plan" -> String.format(
                    "短期目标：快速融入团队，掌握%s相关技能；长期目标：成为%s领域的专业人才。",
                    position, position);
            case "项目经历", "projects" ->
                    "建议填写 2-3 个与申请职位相关的项目经历，突出你的角色和技术栈。";
            default -> "建议结合职位要求，突出你的相关经验和技能。";
        };
    }

    private String formatSkills(Map<String, Integer> skills) {
        Map<String, String> nameMap = Map.of(
                "communication", "沟通能力", "collaboration", "协作能力",
                "problem_solving", "问题解决能力");
        return skills.entrySet().stream()
                .filter(e -> e.getValue() >= 60)
                .map(e -> nameMap.getOrDefault(e.getKey(), e.getKey()))
                .reduce((a, b) -> a + "、" + b)
                .orElse("学习能力");
    }
}