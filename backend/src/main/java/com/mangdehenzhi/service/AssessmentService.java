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
import java.util.Map;
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

    /**
     * 提交测评
     * 1) 校验并计算得分（事务外）
     * 2) 调用 AI 分析（事务外，避免长耗时占用 DB 连接）
     * 3) 落库结果 + 签发证书（事务内）
     */
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

        // AI 分析（事务外执行，不占用 DB 连接）
        String analysisJson = aiService.analyzeAssessmentResult(
                assessment.getTitle(), request.getDimensionScores(), totalScore);
        String recommendations = aiService.generateRecommendations(
                assessment.getTitle(), request.getDimensionScores());

        // 持久化 + 签发证书（事务内）
        return saveResultAndIssueCertificate(
                request.getAssessmentId(), userId, totalScore, passed,
                request.getDimensionScores(), analysisJson, recommendations);
    }

    @Transactional
    protected AssessmentResultDTO saveResultAndIssueCertificate(
            Long assessmentId, Long userId, int totalScore, boolean passed,
            Map<String, Integer> dimensionScores, String analysisJson, String recommendations) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", assessmentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        AssessmentResult result = AssessmentResult.builder()
                .assessment(assessment)
                .user(user)
                .score(totalScore)
                .passed(passed)
                .dimensionScores(dimensionScores)
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
    public AssessmentResultDTO getResultById(Long resultId, Long userId) {
        AssessmentResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("AssessmentResult", resultId));
        // 仅本人可查看自己的测评结果
        if (!result.getUser().getId().equals(userId)) {
            throw new BusinessException(403, "无权访问该测评结果");
        }
        return AssessmentResultDTO.fromEntity(result);
    }
}