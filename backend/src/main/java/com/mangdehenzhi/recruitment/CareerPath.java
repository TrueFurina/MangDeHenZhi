package com.mangdehenzhi.recruitment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 职业路径数据 — 基于Kaggle College Student Career Selection / AI Job Postings 数据集
 * 每个职业路径包含：所需技能、推荐课程、行业需求指数、薪资范围
 */
@Entity
@Table(name = "career_paths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CareerPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;           // 职业名称

    @Column(length = 50)
    private String category;        // 分类: TECH / AI / BUSINESS / DESIGN / SOFT_SKILLS

    @Column(columnDefinition = "TEXT")
    private String description;     // 职业描述

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;  // 所需技能（JSON数组）

    @Column(columnDefinition = "TEXT")
    private String recommendedCourses; // 推荐课程（JSON数组）

    @Column(length = 50)
    private String salaryRange;     // 薪资范围

    private Double demandScore;     // 需求指数 (0-100)

    private Double growthPotential; // 成长潜力 (0-100)

    @Column(length = 20)
    private String difficulty;      // 入门难度: EASY / MEDIUM / HARD

    @Column(columnDefinition = "TEXT")
    private String typicalTasks;    // 典型工作任务

    @Column(length = 50)
    private String relatedMajors;   // 对口专业

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}