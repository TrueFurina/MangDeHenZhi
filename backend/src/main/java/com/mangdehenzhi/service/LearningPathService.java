package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.recruitment.CareerPath;
import com.mangdehenzhi.recruitment.CareerPathRepository;
import com.mangdehenzhi.recruitment.SkillTrend;
import com.mangdehenzhi.recruitment.SkillTrendRepository;
import com.mangdehenzhi.repository.AssessmentResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 个性化学习路径推荐引擎
 * 基于：
 *   - 用户技能测评结果 (dimensionScores)
 *   - Kaggle Skill Demand / Scarcity 数据
 *   - CareerPath 职业路径数据
 *   - 已有课程数据
 * 输出：推荐的学习路径（技能短板 → 推荐课程 → 职业方向）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningPathService {

    private final CareerPathRepository careerPathRepository;
    private final SkillTrendRepository skillTrendRepository;
    private final AssessmentResultRepository assessmentResultRepository;

    /**
     * 获取用户最新测评得分，无记录时返回默认值
     */
    public Map<String, Integer> getLatestAssessmentScores(Long userId) {
        var results = assessmentResultRepository.findByUserId(userId);
        if (results.isEmpty()) {
            return Map.of("communication", 50, "collaboration", 50, "problem_solving", 50);
        }
        return results.get(results.size() - 1).getDimensionScores();
    }

    /**
     * 生成个性化学习路径
     * @param skillScores 用户技能测评得分 (dimension -> score)
     * @param topN 推荐职业路径数量
     */
    public LearningPathResult generateLearningPath(Map<String, Integer> skillScores, int topN) {
        // 1. 找出薄弱技能
        List<SkillGap> gaps = findSkillGaps(skillScores);

        // 2. 匹配推荐职业路径
        List<CareerPathRecommendation> careerRecs = recommendCareers(skillScores, topN);

        // 3. 生成学习计划
        List<LearningStep> steps = generateLearningSteps(gaps, careerRecs);

        // 4. 计算综合评分
        double overallScore = calculateOverallScore(skillScores);

        return new LearningPathResult(overallScore, gaps, careerRecs, steps);
    }

    /**
     * 分析技能短板
     */
    private List<SkillGap> findSkillGaps(Map<String, Integer> skillScores) {
        // 维度名称映射（含新维度）
        Map<String, String> dimToSkill = Map.of(
            "communication", "沟通能力",
            "collaboration", "团队协作",
            "problem_solving", "问题解决能力",
            "computer_basics", "计算机基础",
            "data_analysis", "数据分析",
            "team_collaboration", "团队协作"
        );

        List<SkillGap> gaps = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : skillScores.entrySet()) {
            String dim = entry.getKey();
            int score = entry.getValue();
            String skillName = dimToSkill.getOrDefault(dim, dim);

            // 查找对应的技能趋势数据
            Optional<SkillTrend> trend = skillTrendRepository
                .findBySkillNameContainingIgnoreCase(skillName)
                .stream().findFirst();

            double demand = trend.map(SkillTrend::getDemandIndex).orElse(50.0);
            double scarcity = trend.map(SkillTrend::getScarcityIndex).orElse(50.0);

            // 知识图谱关联技能推荐
            List<String> relatedSkills = getRelatedSkills(skillName);

            gaps.add(new SkillGap(skillName, score, demand, scarcity,
                score < 60 ? "CRITICAL" : score < 75 ? "IMPROVE" : "MAINTAIN",
                relatedSkills));
        }

        // 按优先度排序（得分越低越优先，同时考虑需求指数）
        gaps.sort((a, b) -> {
            int scoreCompare = Integer.compare(a.score(), b.score());
            if (scoreCompare != 0) return scoreCompare;
            return Double.compare(b.demandIndex(), a.demandIndex());
        });
        return gaps;
    }

    /**
     * 知识图谱：技能关联关系映射
     * 返回给定技能的相关推荐技能
     */
    private List<String> getRelatedSkills(String skillName) {
        Map<String, List<String>> skillGraph = Map.of(
            "沟通能力", List.of("团队协作", "领导力", "情绪智力"),
            "团队协作", List.of("沟通能力", "项目管理", "领导力"),
            "问题解决能力", List.of("数据分析思维", "批判性思维", "结构化思维"),
            "计算机基础", List.of("Python", "数据科学", "网络安全"),
            "数据分析", List.of("SQL", "Python", "数据可视化", "机器学习"),
            "领导力", List.of("沟通能力", "团队协作", "项目管理"),
            "项目管理", List.of("团队协作", "沟通能力", "时间管理")
        );
        return skillGraph.getOrDefault(skillName, List.of());
    }

    /**
     * 推荐职业路径
     */
    private List<CareerPathRecommendation> recommendCareers(Map<String, Integer> skillScores, int topN) {
        List<CareerPath> allPaths = careerPathRepository.findAll();
        if (allPaths.isEmpty()) return List.of();

        // 计算每个职业路径与用户技能的匹配度
        List<CareerPathRecommendation> recs = allPaths.stream()
            .map(path -> {
                double matchScore = calculateCareerMatch(path, skillScores);
                return new CareerPathRecommendation(path, matchScore);
            })
            .sorted((a, b) -> Double.compare(b.matchScore(), a.matchScore()))
            .limit(topN > 0 ? topN : 5)
            .collect(Collectors.toList());

        return recs;
    }

    /**
     * 计算职业路径与用户技能的匹配度
     */
    private double calculateCareerMatch(CareerPath path, Map<String, Integer> skillScores) {
        double score = 50.0; // 基础分

        // 需求指数加分
        if (path.getDemandScore() != null) {
            score += path.getDemandScore() * 0.2;
        }

        // 技能匹配加分
        String skills = path.getRequiredSkills();
        if (skills != null) {
            for (Map.Entry<String, Integer> entry : skillScores.entrySet()) {
                String skillName = switch (entry.getKey()) {
                    case "communication" -> "沟通";
                    case "collaboration" -> "团队";
                    case "problem_solving" -> "问题解决";
                    default -> entry.getKey();
                };
                if (skills.contains(skillName) && entry.getValue() >= 60) {
                    score += 10;
                }
            }
        }

        return Math.min(100, Math.max(0, score));
    }

    /**
     * 生成学习步骤
     */
    private List<LearningStep> generateLearningSteps(List<SkillGap> gaps, List<CareerPathRecommendation> careerRecs) {
        List<LearningStep> steps = new ArrayList<>();
        int order = 1;

        // 阶段1：基础巩固（针对薄弱技能）
        List<SkillGap> criticalGaps = gaps.stream()
            .filter(g -> "CRITICAL".equals(g.priority))
            .collect(Collectors.toList());
        if (!criticalGaps.isEmpty()) {
            List<String> focusSkills = criticalGaps.stream()
                .map(SkillGap::skillName)
                .collect(Collectors.toList());
            steps.add(new LearningStep(order++, "📚 基础巩固",
                "针对薄弱环节系统学习",
                focusSkills,
                "建议学习时间：2-3周",
                "完成基础课程学习并重新测评"));
        }

        // 阶段2：能力提升
        List<SkillGap> improveGaps = gaps.stream()
            .filter(g -> "IMPROVE".equals(g.priority))
            .collect(Collectors.toList());
        if (!improveGaps.isEmpty()) {
            List<String> focusSkills = improveGaps.stream()
                .map(SkillGap::skillName)
                .collect(Collectors.toList());
            steps.add(new LearningStep(order++, "🚀 能力提升",
                "进阶技能训练",
                focusSkills,
                "建议学习时间：3-4周",
                "完成进阶课程并参与项目实践"));
        }

        // 阶段3：职业方向
        if (!careerRecs.isEmpty()) {
            CareerPathRecommendation top = careerRecs.get(0);
            steps.add(new LearningStep(order++, "🎯 职业定向",
                "推荐方向：" + top.path().getTitle(),
                List.of(top.path().getTitle()),
                "需求指数：" + String.format("%.0f", top.path().getDemandScore() != null ? top.path().getDemandScore() : 0) +
                " 薪资范围：" + (top.path().getSalaryRange() != null ? top.path().getSalaryRange() : "面议"),
                "完成职业定向课程并准备相关认证"));
        }

        // 阶段4：实战应用
        steps.add(new LearningStep(order++, "💪 实战应用",
            "项目实战与模拟",
            List.of("综合项目实践"),
            "建议时间：4-6周",
            "完成综合项目并准备面试"));

        return steps;
    }

    private double calculateOverallScore(Map<String, Integer> skillScores) {
        if (skillScores.isEmpty()) return 0;
        return skillScores.values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0);
    }

    // ===== DTOs =====

    public record SkillGap(String skillName, int score, double demandIndex, double scarcityIndex, String priority, List<String> relatedSkills) {}
    public record CareerPathRecommendation(CareerPath path, double matchScore) {}
    public record LearningStep(int order, String phase, String description, List<String> focusSkills, String duration, String goal) {}
    public record LearningPathResult(double overallScore, List<SkillGap> gaps, List<CareerPathRecommendation> careerPaths, List<LearningStep> steps) {}
}