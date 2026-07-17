package com.mangdehenzhi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class AssessmentSubmitRequest {
    @NotNull(message = "测评ID不能为空")
    private Long assessmentId;

    @NotEmpty(message = "维度得分不能为空")
    private Map<String, @NotNull(message = "维度得分不能为空") Integer> dimensionScores;

    private String answers;
}