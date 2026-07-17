package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.enums.CourseCategory;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程推荐服务 - 基于AI分析结果的个性化推荐
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CourseRepository courseRepository;

    /**
     * 根据测评结果推荐课程
     */
    public List<Course> recommendCourses(AssessmentResult result) {
        // 基于薄弱维度推荐
        Map<String, Integer> dimScores = result.getDimensionScores();
        List<String> weakDimensions = dimScores.entrySet().stream()
                .filter(e -> e.getValue() < 60)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 根据薄弱环节映射到课程类别
        List<CourseCategory> targetCategories = mapDimensionsToCategories(weakDimensions);

        // 查找对应类别课程
        Set<Course> recommended = new LinkedHashSet<>();
        for (CourseCategory category : targetCategories) {
            recommended.addAll(courseRepository.findByCategory(category));
        }

        // 补充热门课程
        if (recommended.size() < 5) {
            recommended.addAll(courseRepository.findByPublishedTrue());
        }

        return recommended.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * 生成个性化学习路径
     */
    public List<String> generateLearningPath(AssessmentResult result) {
        List<String> path = new ArrayList<>();
        Map<String, Integer> scores = result.getDimensionScores();

        if (scores.values().stream().anyMatch(s -> s < 60)) {
            path.add("📚 基础巩固阶段 - 针对薄弱环节系统学习");
        }
        if (scores.values().stream().anyMatch(s -> s >= 60 && s < 80)) {
            path.add("🚀 能力提升阶段 - 进阶技能训练");
        }
        path.add("🎯 实战应用阶段 - 项目实战与模拟");
        path.add("🏆 认证冲刺阶段 - 准备最终测评与认证");

        return path;
    }

    private List<CourseCategory> mapDimensionsToCategories(List<String> dimensions) {
        Map<String, CourseCategory> mapping = Map.of(
            "沟通能力", CourseCategory.SOFT_SKILLS,
            "协作能力", CourseCategory.SOFT_SKILLS,
            "问题解决", CourseCategory.TECHNOLOGY,
            "技术能力", CourseCategory.TECHNOLOGY,
            "设计思维", CourseCategory.DESIGN,
            "语言表达", CourseCategory.LANGUAGE,
            "商业思维", CourseCategory.BUSINESS
        );

        return dimensions.stream()
                .map(dim -> mapping.getOrDefault(dim, CourseCategory.TECHNOLOGY))
                .distinct()
                .collect(Collectors.toList());
    }
}