package com.mangdehenzhi.recruitment;

import com.mangdehenzhi.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户网申填报记录
 */
@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(length = 200)
    private String companyName;     // 公司名称

    @Column(length = 200)
    private String positionName;    // 职位名称

    @Column(columnDefinition = "TEXT")
    private String formData;        // 填报表单数据 (JSON)

    @Column(length = 20)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT / SUBMITTED / REJECTED / ACCEPTED

    @Column(columnDefinition = "TEXT")
    private String aiSuggestions;   // AI 填报建议

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