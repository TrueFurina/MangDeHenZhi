package com.mangdehenzhi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户学习进度 — 追踪每个课时的学习状态
 */
@Entity
@Table(name = "user_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "lesson_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private CourseLesson lesson;

    @Column(length = 20)
    @Builder.Default
    private String status = "NOT_STARTED"; // NOT_STARTED / IN_PROGRESS / COMPLETED

    @Column(columnDefinition = "TEXT")
    private String socraticAnswers;   // 苏格拉底问答记录（JSON）

    @Column(columnDefinition = "TEXT")
    private String feynmanExplanation; // 费曼学习法的解释

    @Column(columnDefinition = "TEXT")
    private String practiceResults;   // 练习结果（JSON）

    @Builder.Default
    private Integer score = 0;         // 理解度评分（0-100）

    @Builder.Default
    private Integer attempts = 0;

    private LocalDateTime completedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}