package com.mangdehenzhi.recruitment;

import com.mangdehenzhi.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 职业路径接口 — 基于Kaggle数据的职业规划与学习路径推荐
 */
@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
public class CareerPathController {

    private final CareerPathRepository repository;

    @GetMapping("/paths")
    public ResponseEntity<ApiResponse<List<CareerPath>>> getAllPaths() {
        return ResponseEntity.ok(ApiResponse.success(repository.findAll()));
    }

    @GetMapping("/paths/top")
    public ResponseEntity<ApiResponse<List<CareerPath>>> getTopPaths() {
        return ResponseEntity.ok(ApiResponse.success(repository.findTop10ByOrderByDemandScoreDesc()));
    }

    @GetMapping("/paths/{id}")
    public ResponseEntity<ApiResponse<CareerPath>> getPath(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                repository.findById(id).orElseThrow(() -> new RuntimeException("职业路径不存在"))));
    }

    @GetMapping("/paths/category/{category}")
    public ResponseEntity<ApiResponse<List<CareerPath>>> getPathsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.success(repository.findByCategoryOrderByDemandScoreDesc(category)));
    }

    @GetMapping("/paths/categories")
    public ResponseEntity<ApiResponse<Map<String, List<CareerPath>>>> getPathsGrouped() {
        Map<String, List<CareerPath>> grouped = repository.findAll().stream()
                .collect(Collectors.groupingBy(CareerPath::getCategory));
        return ResponseEntity.ok(ApiResponse.success(grouped));
    }
}