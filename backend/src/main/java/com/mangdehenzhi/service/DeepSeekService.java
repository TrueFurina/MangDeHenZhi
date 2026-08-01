package com.mangdehenzhi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mangdehenzhi.recruitment.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * DeepSeek 大模型 API 服务
 * 从环境变量 DEEPSEEK_API_KEY 读取 API Key
 * API 文档: https://platform.deepseek.com/api-docs
 */
@Slf4j
@Service
public class DeepSeekService {

    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String MODEL = "deepseek-chat";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final boolean enabled;

    public DeepSeekService(
            @Value("${DEEPSEEK_API_KEY:#{null}}") String apiKey,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.enabled = apiKey != null && !apiKey.isBlank();

        if (enabled) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(Duration.ofSeconds(3));
            requestFactory.setReadTimeout(Duration.ofSeconds(30));
            this.restClient = RestClient.builder()
                    .baseUrl(DEEPSEEK_API_URL)
                    .requestFactory(requestFactory)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .build();
            log.info("DeepSeek API 已启用，使用模型: {}", MODEL);
        } else {
            this.restClient = null;
            log.warn("DEEPSEEK_API_KEY 未设置，将使用模拟 AI 分析");
        }
    }

    /**
     * 调用 DeepSeek Chat 生成测评分析报告
     */
    public String analyzeAssessmentResult(String title, Map<String, Integer> dimensionScores, int totalScore) {
        if (!enabled) return null;

        String prompt = String.format("""
            你是一个专业的职业技能评估分析师。请对以下测评结果进行分析，返回 JSON 格式（不要 markdown 包裹）：

            测评名称: %s
            总分: %d
            各维度得分: %s

            请按以下 JSON 结构返回：
            {
              "overallAssessment": "总体评价（一句话概括）",
              "strengthDimensions": {"维度名": 分数},
              "weaknessDimensions": {"维度名": 分数},
              "improvementSuggestions": {"维度名": "改进建议"},
              "aiConfidence": 0.85
            }
            """, title, totalScore, dimensionScores);

        return callDeepSeek(prompt);
    }

    /**
     * 调用 DeepSeek Chat 生成个性化学习推荐
     */
    public String generateRecommendations(String title, Map<String, Integer> dimensionScores) {
        if (!enabled) return null;

        String prompt = String.format("""
            你是一个智能学习规划师。请根据以下测评结果为用户推荐学习方案，返回 JSON 格式（不要 markdown 包裹）：

            测评名称: %s
            各维度得分: %s

            请按以下 JSON 结构返回：
            {
              "recommendedCourses": ["推荐课程1", "推荐课程2", "推荐课程3"],
              "learningPath": ["阶段1描述", "阶段2描述", "阶段3描述"],
              "estimatedImprovementTime": "预计提升时间",
              "practiceSuggestions": ["练习建议1", "练习建议2", "练习建议3"]
            }
            """, title, dimensionScores);

        return callDeepSeek(prompt);
    }

    private String callDeepSeek(String prompt) {
        try {
            // 构建请求体
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", MODEL);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2048);

            ArrayNode messages = objectMapper.createArrayNode();
            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个专业的职业技能评估与学习规划助手。请始终用中文回复，只返回 JSON 格式数据。");
            messages.add(systemMsg);

            ObjectNode userMsg = objectMapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            requestBody.set("messages", messages);

            // 调用 API
            String response = restClient.post()
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            // 解析响应
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            // 清理可能的 markdown 包裹
            content = content.trim();
            if (content.startsWith("```")) {
                content = content.replaceAll("```(json)?", "").trim();
            }

            log.debug("DeepSeek API 响应成功，content 长度: {}", content.length());
            return content;

        } catch (Exception e) {
            log.error("调用 DeepSeek API 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 通用对话接口 — AI 导师聊天
     */
    public String chat(String systemInstruction, List<Map<String, String>> conversation) {
        if (!enabled) return null;
        StringBuilder sb = new StringBuilder(systemInstruction).append("\n\n对话历史:\n");
        conversation.forEach(m -> sb.append(m.get("role")).append(": ").append(m.get("content")).append("\n"));
        return callDeepSeek(sb.toString());
    }

    /**
     * 生成网申填写建议
     */
    public String generateApplicationSuggestions(String companyName, String positionName,
                                                 String jobDesc, Map<String, Integer> userSkills,
                                                 List<String> formFields) {
        if (!enabled) return null;
        String prompt = String.format("""
            你是网申填报助手。请为以下职位生成网申各字段填写建议，返回 JSON 对象（不要 markdown 包裹）：
            公司: %s, 职位: %s
            职位描述: %s
            用户技能: %s
            需要生成的字段: %s
            """, companyName, positionName, jobDesc, userSkills, formFields);
        return callDeepSeek(prompt);
    }

    /**
     * 分析简历与职位匹配度，给出优化建议
     */
    public String analyzeResumeFit(String resumeText, String jobDescription) {
        if (!enabled) return null;
        String prompt = String.format("""
            你是简历优化专家。请分析以下简历与目标职位的匹配度，返回 JSON（不要 markdown 包裹）：
            {"matchScore": 0-100, "strengths": [...], "weaknesses": [...], "improvements": [...]}
            简历: %s
            职位要求: %s
            """, resumeText, jobDescription);
        return callDeepSeek(prompt);
    }

    /**
     * 基于技能画像匹配职位
     */
    public String analyzeJobMatch(Map<String, Integer> skillScores, List<Job> jobs) {
        if (!enabled) return null;
        List<String> jobDescs = jobs.stream()
                .map(j -> j.getTitle() + " - " + j.getCompany() + ": " + j.getRequirements())
                .toList();
        String prompt = String.format("""
            你是职业匹配专家。基于用户技能画像，从以下职位中推荐最匹配的，返回 JSON（不要 markdown 包裹）：
            用户技能: %s
            职位列表: %s
            """, skillScores, jobDescs);
        return callDeepSeek(prompt);
    }

    /**
     * 生成苏格拉底式提问
     */
    public String generateSocraticQuestions(String topic, String content) {
        if (!enabled) return null;
        String prompt = String.format("""
            你是苏格拉底式学习导师。基于以下知识点生成引导式提问，帮助学习者主动思考，返回 JSON（不要 markdown 包裹）：
            知识点: %s
            内容: %s
            """, topic, content);
        return callDeepSeek(prompt);
    }

    /**
     * 评估费曼学习法讲解
     */
    public String evaluateFeynmanExplanation(String concept, String userExplanation) {
        if (!enabled) return null;
        String prompt = String.format("""
            你是费曼学习法评估专家。评估学习者的讲解，返回 JSON（不要 markdown 包裹）：
            {"score": 0-100, "clarity": "...", "gaps": [...], "suggestions": [...]}
            概念: %s
            学习者讲解: %s
            """, concept, userExplanation);
        return callDeepSeek(prompt);
    }

    /**
     * 生成跟进提问
     */
    public String generateFollowUpQuestion(String topic, String previousAnswer) {
        if (!enabled) return null;
        String prompt = String.format("""
            你是学习导师。基于学习者的回答，生成一个加深理解的跟进问题（直接返回问题文本）：
            主题: %s
            学习者回答: %s
            """, topic, previousAnswer);
        return callDeepSeek(prompt);
    }

    public boolean isEnabled() {
        return enabled;
    }
}