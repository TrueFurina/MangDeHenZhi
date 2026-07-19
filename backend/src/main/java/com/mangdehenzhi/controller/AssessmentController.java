package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.AssessmentResultDTO;
import com.mangdehenzhi.dto.AssessmentSubmitRequest;
import com.mangdehenzhi.dto.PageDTO;
import com.mangdehenzhi.entity.Assessment;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Assessment>>> getAllAssessments() {
        return ResponseEntity.ok(ApiResponse.success(assessmentService.getAllAssessments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Assessment>> getAssessment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(assessmentService.getAssessmentById(id)));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<AssessmentResultDTO>> submitAssessment(
            @AuthenticationPrincipal User user,
            @RequestBody AssessmentSubmitRequest request) {
        AssessmentResultDTO result = assessmentService.submitAssessment(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("测评提交成功", result));
    }

    @GetMapping("/results")
    public ResponseEntity<ApiResponse<PageDTO<AssessmentResultDTO>>> getMyResults(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var results = assessmentService.getUserResults(user.getId());
        int total = results.size();
        int from = page * size;
        int to = Math.min(from + size, total);
        List<AssessmentResultDTO> pageContent = from < total ? results.subList(from, to) : List.of();
        return ResponseEntity.ok(ApiResponse.success(
                PageDTO.of(pageContent, page, size, total)));
    }

    @GetMapping("/results/{resultId}")
    public ResponseEntity<ApiResponse<AssessmentResultDTO>> getResult(
            @AuthenticationPrincipal User user, @PathVariable Long resultId) {
        return ResponseEntity.ok(ApiResponse.success(assessmentService.getResultById(resultId, user.getId())));
    }
}