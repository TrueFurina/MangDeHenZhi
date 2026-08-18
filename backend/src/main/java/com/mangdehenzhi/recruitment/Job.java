package com.mangdehenzhi.recruitment;

import com.mangdehenzhi.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 职位/JD 实体 — 来自公开校招信息
 */
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;           // 职位名称

    @Column(nullable = false, length = 200)
    private String company;         // 公司名称

    @Column(length = 100)
    private String industry;        // 行业

    @Column(length = 100)
    private String location;        // 工作地点

    @Column(columnDefinition = "TEXT")
    private String description;     // 职位描述

    @Column(columnDefinition = "TEXT")
    private String requirements;    // 任职要求

    @Column(length = 50)
    private String degree;          // 学历要求: 本科/硕士/博士

    @Column(length = 50)
    private String major;           // 专业要求

    @Column(length = 100)
    private String salary;          // 薪资范围

    @Column(length = 500)
    private String applyUrl;        // 网申链接

    @Column(length = 100)
    private String source;          // 信息来源

    @Builder.Default
    private Boolean active = true;

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