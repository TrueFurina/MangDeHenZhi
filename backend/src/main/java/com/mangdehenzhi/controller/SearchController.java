package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 搜索端点 — 课程搜索
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final CourseRepository courseRepository;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<Course>>> searchCourses(
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int limit) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success(List.of()));
        }
        List<Course> results = courseRepository.findByTitleContainingIgnoreCase(q.trim());
        if (results.size() > limit) {
            results = results.subList(0, limit);
        }
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}