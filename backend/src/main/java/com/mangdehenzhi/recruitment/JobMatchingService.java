package com.mangdehenzhi.recruitment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangdehenzhi.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 职位匹配服务 — AI 智能选岗
 * 基于用户技能画像和公开职位信息进行匹配推荐
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobMatchingService {

    private final JobRepository jobRepository;
    private final DeepSeekService deepSeekService;
    private final ObjectMapper objectMapper;

    /**
     * 基于用户技能维度分数推荐匹配的职位
     */
    public List<JobMatchResult> matchJobs(Map<String, Integer> skillScores, int limit) {
        List<Job> allJobs = jobRepository.findByActiveTrue();
        if (allJobs.isEmpty()) return List.of();

        // 计算每个职位的匹配度
        List<JobMatchResult> results = allJobs.stream()
                .map(job -> {
                    double matchScore = calculateMatchScore(job, skillScores);
                    return new JobMatchResult(job, matchScore, generateMatchReason(job, matchScore));
                })
                .sorted((a, b) -> Double.compare(b.matchScore(), a.matchScore()))
                .limit(limit > 0 ? limit : 20)
                .collect(Collectors.toList());

        // 如果 DeepSeek 可用，尝试 AI 增强推荐
        if (deepSeekService.isEnabled()) {
            try {
                String aiRecommendation = deepSeekService.analyzeJobMatch(skillScores, allJobs.subList(0, Math.min(5, allJobs.size())));
                log.info("AI 职位推荐分析完成: {}", aiRecommendation);
            } catch (Exception e) {
                log.warn("AI 增强推荐失败，使用本地匹配: {}", e.getMessage());
            }
        }

        return results;
    }

    /**
     * 根据关键词搜索职位
     */
    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        String kw = keyword.trim();
        Set<Job> results = new LinkedHashSet<>();
        results.addAll(jobRepository.findByTitleContainingIgnoreCase(kw));
        results.addAll(jobRepository.findByCompanyContainingIgnoreCase(kw));
        results.addAll(jobRepository.findByIndustryContainingIgnoreCase(kw));
        results.addAll(jobRepository.findByLocationContainingIgnoreCase(kw));
        return results.stream().limit(50).collect(Collectors.toList());
    }

    /**
     * 计算技能与职位的匹配度 (0-100)
     */
    private double calculateMatchScore(Job job, Map<String, Integer> skillScores) {
        double score = 50.0; // 基础分

        String combined = (job.getTitle() + " " + job.getDescription() + " " + job.getRequirements()).toLowerCase();

        // 技能匹配加分
        for (Map.Entry<String, Integer> skill : skillScores.entrySet()) {
            String skillName = skill.getKey().toLowerCase();
            int skillLevel = skill.getValue();

            // 技能关键词映射
            Map<String, List<String>> skillKeywords = Map.of(
                "communication", List.of("沟通", "表达", "交流", "communication"),
                "collaboration", List.of("协作", "团队", "合作", "collaboration", "team"),
                "problem_solving", List.of("解决", "分析", "问题", "problem", "analyze", "solve"),
                "技术能力", List.of("java", "python", "编程", "开发", "技术", "programming", "software"),
                "设计思维", List.of("设计", "ui", "ux", "设计思维", "design"),
                "商业思维", List.of("商业", "市场", "运营", "business", "marketing")
            );

            List<String> keywords = skillKeywords.getOrDefault(skillName, List.of(skillName));
            boolean hasMatch = keywords.stream().anyMatch(kw -> combined.contains(kw));
            if (hasMatch && skillLevel >= 60) {
                score += 10;
            } else if (hasMatch) {
                score += 5;
            }
        }

        // 学历匹配
        if (job.getDegree() != null && !job.getDegree().isBlank()) {
            // 假设本科及以上都匹配
            score += 5;
        }

        return Math.min(100, Math.max(0, score));
    }

    private String generateMatchReason(Job job, double score) {
        if (score >= 80) return "🔥 高度匹配：你的技能与该职位要求非常契合";
        if (score >= 65) return "👍 推荐投递：技能匹配度良好，建议尝试";
        if (score >= 50) return "📌 可以考虑：部分技能符合要求";
        return "💡 了解参考：可作为职业发展方向参考";
    }

    /**
     * 匹配结果 DTO
     */
    public record JobMatchResult(Job job, double matchScore, String reason) {}
}