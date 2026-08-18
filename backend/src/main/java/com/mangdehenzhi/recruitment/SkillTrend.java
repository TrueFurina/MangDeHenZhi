package com.mangdehenzhi.recruitment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 技能趋势数据 — 来自 Kaggle Skill Demand / Scarcity / AI Requirements 数据集
 * 用于展示技能需求热力图和智能课程推荐
 */
@Entity
@Table(name = "skill_trends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SkillTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String skillName;       // 技能名称

    @Column(length = 50)
    private String category;        // 技能分类: TECHNOLOGY / SOFT_SKILLS / BUSINESS / AI / LANGUAGE

    @Column(nullable = false)
    private Double demandIndex;     // 需求指数 (0-100)

    @Column(nullable = false)
    private Double scarcityIndex;   // 稀缺指数 (0-100)

    @Column(nullable = false)
    private Double growthRate;      // 增长率 (%)

    @Column(length = 20)
    private String trend;           // 趋势: UP / STABLE / DOWN

    @Column(columnDefinition = "TEXT")
    private String description;     // 技能描述

    @Column(length = 200)
    private String relatedCourses;  // 关联课程推荐（JSON数组）

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}