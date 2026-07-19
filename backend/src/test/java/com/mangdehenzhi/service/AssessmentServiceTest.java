package com.mangdehenzhi.service;

import com.mangdehenzhi.dto.AssessmentSubmitRequest;
import com.mangdehenzhi.dto.AssessmentResultDTO;
import com.mangdehenzhi.dto.RegisterRequest;
import com.mangdehenzhi.entity.Assessment;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.AssessmentStatus;
import com.mangdehenzhi.enums.DifficultyLevel;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.repository.AssessmentRepository;
import com.mangdehenzhi.repository.AssessmentResultRepository;
import com.mangdehenzhi.repository.CertificationRepository;
import com.mangdehenzhi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AssessmentServiceTest {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentResultRepository resultRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User testUser;
    private Assessment testAssessment;

    @BeforeEach
    void setUp() {
        certificationRepository.deleteAll();
        resultRepository.deleteAll();
        assessmentRepository.deleteAll();
        userRepository.deleteAll();

        // 创建测试用户
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("assessuser");
        reg.setPassword("password123");
        reg.setEmail("assess@example.com");
        testUser = userRepository.findByUsername("assessuser").orElse(null);
        if (testUser == null) {
            userService.register(reg);
            testUser = userRepository.findByUsername("assessuser").orElseThrow();
        }

        // 创建测试测评
        testAssessment = Assessment.builder()
                .title("综合技能测评")
                .description("测试测评")
                .difficulty(DifficultyLevel.ADAPTIVE)
                .dimensions(List.of("communication", "collaboration", "problem_solving"))
                .duration(60)
                .totalScore(300)
                .passScore(180)
                .status(AssessmentStatus.PENDING)
                .build();
        testAssessment = assessmentRepository.save(testAssessment);
    }

    @Test
    void getAllAssessments_ShouldReturnList() {
        List<Assessment> assessments = assessmentService.getAllAssessments();
        assertFalse(assessments.isEmpty());
        assertEquals(1, assessments.size());
    }

    @Test
    void getAssessmentById_ShouldReturnAssessment() {
        Assessment found = assessmentService.getAssessmentById(testAssessment.getId());
        assertNotNull(found);
        assertEquals(testAssessment.getTitle(), found.getTitle());
    }

    @Test
    void getAssessmentById_WithInvalidId_ShouldThrow() {
        assertThrows(ResourceNotFoundException.class,
                () -> assessmentService.getAssessmentById(999L));
    }

    @Test
    void submitAssessment_WithValidData_ShouldReturnResult() {
        AssessmentSubmitRequest request = new AssessmentSubmitRequest();
        request.setAssessmentId(testAssessment.getId());
        request.setDimensionScores(Map.of(
                "communication", 80,
                "collaboration", 70,
                "problem_solving", 90
        ));

        AssessmentResultDTO result = assessmentService.submitAssessment(testUser.getId(), request);

        assertNotNull(result);
        assertEquals(240, result.getScore());
        assertTrue(result.getPassed());
        assertEquals(testAssessment.getTitle(), result.getAssessmentTitle());
        assertNotNull(result.getAiAnalysis());
        assertNotNull(result.getRecommendations());
    }

    @Test
    void submitAssessment_WithFailingScore_ShouldNotPass() {
        AssessmentSubmitRequest request = new AssessmentSubmitRequest();
        request.setAssessmentId(testAssessment.getId());
        request.setDimensionScores(Map.of(
                "communication", 30,
                "collaboration", 40,
                "problem_solving", 50
        ));

        AssessmentResultDTO result = assessmentService.submitAssessment(testUser.getId(), request);

        assertFalse(result.getPassed());
        assertEquals(120, result.getScore());
    }

    @Test
    void getUserResults_ShouldReturnUserResults() {
        AssessmentSubmitRequest request = new AssessmentSubmitRequest();
        request.setAssessmentId(testAssessment.getId());
        request.setDimensionScores(Map.of("communication", 80, "collaboration", 70, "problem_solving", 90));
        assessmentService.submitAssessment(testUser.getId(), request);

        List<AssessmentResultDTO> results = assessmentService.getUserResults(testUser.getId());
        assertEquals(1, results.size());
    }

    @Test
    void submitAssessment_WithInvalidDimensionScore_ShouldThrow() {
        AssessmentSubmitRequest request = new AssessmentSubmitRequest();
        request.setAssessmentId(testAssessment.getId());
        request.setDimensionScores(Map.of("communication", 150)); // 超出范围

        assertThrows(Exception.class,
                () -> assessmentService.submitAssessment(testUser.getId(), request));
    }
}