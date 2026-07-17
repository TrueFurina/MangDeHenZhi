package com.mangdehenzhi.dto;

import com.mangdehenzhi.entity.AssessmentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultDTO {
    private Long id;
    private Long assessmentId;
    private String assessmentTitle;
    private Long userId;
    private String username;
    private Integer score;
    private Boolean passed;
    private Map<String, Integer> dimensionScores;
    private String aiAnalysis;
    private String recommendations;
    private LocalDateTime completedAt;

    public static AssessmentResultDTO fromEntity(AssessmentResult result) {
        return AssessmentResultDTO.builder()
                .id(result.getId())
                .assessmentId(result.getAssessment().getId())
                .assessmentTitle(result.getAssessment().getTitle())
                .userId(result.getUser().getId())
                .username(result.getUser().getUsername())
                .score(result.getScore())
                .passed(result.getPassed())
                .dimensionScores(result.getDimensionScores())
                .aiAnalysis(result.getAiAnalysis())
                .recommendations(result.getRecommendations())
                .completedAt(result.getCompletedAt())
                .build();
    }
}