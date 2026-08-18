package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 课程课时 — 支持苏格拉底提问法/费曼学习法的交互式教学单元
 */
@Entity
@Table(name = "course_lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CourseLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;          // 教学内容

    @Column(columnDefinition = "TEXT")
    private String socraticQuestions; // 苏格拉底式提问（JSON数组）

    @Column(columnDefinition = "TEXT")
    private String practiceExercises; // 动手练习题（JSON数组）

    @Column(columnDefinition = "TEXT")
    private String keyConcepts;      // 核心概念（用于费曼检验）

    @Column(nullable = false)
    private Integer sortOrder;       // 排序

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DifficultyLevel difficulty = DifficultyLevel.BEGINNER;

    @Builder.Default
    private Integer estimatedMinutes = 15;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}