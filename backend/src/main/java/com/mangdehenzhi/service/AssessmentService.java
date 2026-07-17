package com.mangdehenzhi.service;

import com.mangdehenzhi.dto.AssessmentResultDTO;
import com.mangdehenzhi.dto.AssessmentSubmitRequest;
import com.mangdehenzhi.entity.Assessment;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.repository.AssessmentRepository;
import com.mangdehenzhi.repository.AssessmentResultRepository;
import com.mangdehenzhi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentResultRepository resultRepository;
    private final CertificationService certificationService;
    private final UserRepository userRepository;
    private final AIService aiService;

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", id));
    }

    @Transactional
    public AssessmentResultDTO submitAssessment(Long userId, AssessmentSubmitRequest request) {
        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", request.getAssessmentId()));

        // 校验维度得分：每个维度得分应在0-100之间
        request.getDimensionScores().values().forEach(score -> {
            if (score == null || score < 0 || score > 100) {
                throw new BusinessException("维度得分必须在0-100之间");
            }
        });

        // 计算总分
        int totalScore = request.getDimensionScores().values().stream()
                .mapToInt(Integer::intValue).sum();

        boolean passed = totalScore >= assessment.getPassScore();

        // AI分析
        String analysisJson = aiService.analyzeAssessmentResult(
                assessment.getTitle(), request.getDimensionScores(), totalScore);

        // AI推荐
        String recommendations = aiService.generateRecommendations(
                assessment.getTitle(), request.getDimensionScores());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        AssessmentResult result = AssessmentResult.builder()
                .assessment(assessment)
                .user(user)
                .score(totalScore)
                .passed(passed)
                .dimensionScores(request.getDimensionScores())
                .aiAnalysis(analysisJson)
                .recommendations(recommendations)
                .completedAt(LocalDateTime.now())
                .build();

        result = resultRepository.save(result);

        // 自动签发证书（如果通过）
        if (passed) {
            try {
                certificationService.issueCertification(user, result);
                log.info("用户 {} 测评通过，已自动签发证书", userId);
            } catch (Exception e) {
                log.error("证书签发失败: userId={}, resultId={}", userId, result.getId(), e);
            }
        }

        return AssessmentResultDTO.fromEntity(result);
    }

    @Transactional(readOnly = true)
    public List<AssessmentResultDTO> getUserResults(Long userId) {
        return resultRepository.findByUserId(userId).stream()
                .map(AssessmentResultDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AssessmentResultDTO getResultById(Long resultId) {
        AssessmentResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("AssessmentResult", resultId));
        return AssessmentResultDTO.fromEntity(result);
    }
}