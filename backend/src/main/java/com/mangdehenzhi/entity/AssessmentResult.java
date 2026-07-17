package com.mangdehenzhi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "assessment_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer score;

    @Builder.Default
    private Boolean passed = false;

    @ElementCollection
    @CollectionTable(name = "assessment_dimension_scores",
            joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "dimension")
    @Column(name = "score")
    private Map<String, Integer> dimensionScores; // 各维度得分

    @Column(columnDefinition = "TEXT")
    private String aiAnalysis; // AI分析报告（JSON）

    @Column(columnDefinition = "TEXT")
    private String recommendations; // 推荐建议

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}