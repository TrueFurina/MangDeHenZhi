package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.AssessmentStatus;
import com.mangdehenzhi.enums.DifficultyLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "creator"})
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @ElementCollection
    @CollectionTable(name = "assessment_dimensions", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "dimension")
    private List<String> dimensions; // 测评维度

    @Column(nullable = false)
    private Integer duration; // 测评时长（分钟）

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private Integer passScore;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AssessmentStatus status = AssessmentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Builder.Default
    private Integer attemptCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}