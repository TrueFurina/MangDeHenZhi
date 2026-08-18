package com.mangdehenzhi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户行为事件 — 专利级数据埋点
 * 记录用户在平台上的所有关键行为，用于学习分析、行为预测和个性化推荐
 */
@Entity
@Table(name = "user_events", indexes = {
    @Index(name = "idx_user_events_user_id", columnList = "userId"),
    @Index(name = "idx_user_events_event_type", columnList = "eventType"),
    @Index(name = "idx_user_events_created_at", columnList = "createdAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String eventType;       // 事件类型: page_view / assessment_start / assessment_complete / course_enroll / lesson_complete / ai_chat / login / logout

    @Column(length = 200)
    private String eventName;       // 事件名称

    @Column(columnDefinition = "TEXT")
    private String eventData;       // 事件数据 (JSON)

    @Column(length = 100)
    private String pageUrl;         // 页面URL

    @Column(length = 50)
    private String sessionId;       // 会话ID

    @Column(length = 45)
    private String ipAddress;       // IP地址

    @Column(length = 200)
    private String userAgent;       // 用户代理

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}