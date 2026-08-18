package com.mangdehenzhi.recruitment;

import com.mangdehenzhi.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 技能趋势数据接口 — 展示技能需求热力图和智能课程推荐
 */
@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillTrendController {

    private final SkillTrendRepository repository;

    @GetMapping("/demand")
    public ResponseEntity<ApiResponse<List<SkillTrend>>> getTopDemandSkills() {
        return ResponseEntity.ok(ApiResponse.success(repository.findTop10ByOrderByDemandIndexDesc()));
    }

    @GetMapping("/scarcity")
    public ResponseEntity<ApiResponse<List<SkillTrend>>> getTopScarcitySkills() {
        return ResponseEntity.ok(ApiResponse.success(repository.findTop10ByOrderByScarcityIndexDesc()));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Map<String, List<SkillTrend>>>> getSkillsByCategory() {
        List<SkillTrend> all = repository.findByActiveTrueOrderByDemandIndexDesc();
        Map<String, List<SkillTrend>> grouped = all.stream()
                .collect(Collectors.groupingBy(SkillTrend::getCategory));
        return ResponseEntity.ok(ApiResponse.success(grouped));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<SkillTrend>>> getTrendingSkills() {
        return ResponseEntity.ok(ApiResponse.success(repository.findByTrendOrderByDemandIndexDesc("UP")));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SkillTrend>>> getAllSkills() {
        return ResponseEntity.ok(ApiResponse.success(repository.findByActiveTrueOrderByDemandIndexDesc()));
    }
}