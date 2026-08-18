package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.UserProgress;
import com.mangdehenzhi.repository.AssessmentResultRepository;
import com.mangdehenzhi.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学习分析仪表盘服务
 * 追踪用户学习效果、技能成长、学习行为数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningAnalyticsService {

    private final AssessmentResultRepository assessmentResultRepository;
    private final UserProgressRepository userProgressRepository;

    /**
     * 获取用户学习分析数据
     */
    public LearningAnalytics getAnalytics(Long userId) {
        // 测评结果分析
        List<AssessmentResult> results = assessmentResultRepository.findByUserId(userId);

        // 学习进度分析
        List<UserProgress> progress = userProgressRepository.findByUserId(userId);

        // 技能成长趋势
        List<SkillTrend> skillTrends = analyzeSkillTrends(results);

        // 学习行为统计
        LearningBehavior behavior = analyzeBehavior(results, progress);

        // 综合评分
        double overallScore = calculateOverallScore(results);

        // 薄弱环节
        List<WeakArea> weakAreas = findWeakAreas(results);

        // 学习建议
        List<String> recommendations = generateRecommendations(weakAreas, behavior);

        return new LearningAnalytics(
            overallScore,
            results.size(),
            progress.size(),
            skillTrends,
            behavior,
            weakAreas,
            recommendations
        );
    }

    private List<SkillTrend> analyzeSkillTrends(List<AssessmentResult> results) {
        if (results.isEmpty()) return List.of();

        // 按时间排序
        List<AssessmentResult> sorted = results.stream()
            .sorted(Comparator.comparing(AssessmentResult::getCompletedAt))
            .collect(Collectors.toList());

        // 提取技能变化趋势
        Map<String, List<Integer>> dimensionHistory = new LinkedHashMap<>();
        for (AssessmentResult r : sorted) {
            if (r.getDimensionScores() != null) {
                r.getDimensionScores().forEach((dim, score) -> {
                    dimensionHistory.computeIfAbsent(dim, k -> new ArrayList<>()).add(score);
                });
            }
        }

        List<SkillTrend> trends = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : dimensionHistory.entrySet()) {
            List<Integer> scores = entry.getValue();
            if (scores.size() >= 2) {
                int first = scores.get(0);
                int last = scores.get(scores.size() - 1);
                int change = last - first;
                trends.add(new SkillTrend(entry.getKey(), first, last, change, scores));
            }
        }
        return trends;
    }

    private LearningBehavior analyzeBehavior(List<AssessmentResult> results, List<UserProgress> progress) {
        long completedAssessments = results.stream().filter(AssessmentResult::getPassed).count();
        long completedLessons = progress.stream()
            .filter(p -> "COMPLETED".equals(p.getStatus())).count();
        long inProgressLessons = progress.stream()
            .filter(p -> "IN_PROGRESS".equals(p.getStatus())).count();

        // 平均得分
        double avgScore = results.stream()
            .mapToInt(AssessmentResult::getScore)
            .average().orElse(0);

        // 学习活跃度
        long recentActivity = results.stream()
            .filter(r -> r.getCompletedAt() != null && r.getCompletedAt().isAfter(LocalDateTime.now().minusDays(7)))
            .count();

        return new LearningBehavior(
            completedAssessments,
            completedLessons,
            inProgressLessons,
            Math.round(avgScore * 100.0) / 100.0,
            recentActivity
        );
    }

    private double calculateOverallScore(List<AssessmentResult> results) {
        if (results.isEmpty()) return 0;
        return results.stream()
            .mapToInt(AssessmentResult::getScore)
            .average()
            .orElse(0);
    }

    private List<WeakArea> findWeakAreas(List<AssessmentResult> results) {
        if (results.isEmpty()) return List.of();

        // 取最新测评结果
        AssessmentResult latest = results.get(results.size() - 1);
        if (latest.getDimensionScores() == null) return List.of();

        List<WeakArea> weakAreas = new ArrayList<>();
        latest.getDimensionScores().forEach((dim, score) -> {
            if (score < 70) {
                weakAreas.add(new WeakArea(dim, score,
                    score < 60 ? "critical" : "warning"));
            }
        });
        weakAreas.sort(Comparator.comparingInt(WeakArea::score));
        return weakAreas;
    }

    private List<String> generateRecommendations(List<WeakArea> weakAreas, LearningBehavior behavior) {
        List<String> recs = new ArrayList<>();

        if (weakAreas.isEmpty()) {
            recs.add("🎉 整体表现优秀！建议挑战更高难度的学习内容");
        } else {
            recs.add("📚 重点关注薄弱环节：" + weakAreas.stream()
                .map(WeakArea::dimension)
                .collect(Collectors.joining("、")));
        }

        if (behavior.recentActivity() == 0) {
            recs.add("💡 最近7天没有学习活动，建议保持学习节奏");
        }

        if (behavior.completedLessons() == 0) {
            recs.add("🎯 还没有完成任何课时，建议开始学习路径");
        }

        recs.add("📈 坚持定期测评，追踪技能成长趋势");
        return recs;
    }

    // ===== DTOs =====

    public record SkillTrend(String dimension, int firstScore, int lastScore, int change, List<Integer> history) {}
    public record LearningBehavior(long completedAssessments, long completedLessons, long inProgressLessons, double avgScore, long recentActivity) {}
    public record WeakArea(String dimension, int score, String severity) {}
    public record LearningAnalytics(
        double overallScore,
        int totalAssessments,
        int totalLessons,
        List<SkillTrend> skillTrends,
        LearningBehavior behavior,
        List<WeakArea> weakAreas,
        List<String> recommendations
    ) {}
}