package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.Assessment;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.AssessmentStatus;
import com.mangdehenzhi.enums.DifficultyLevel;
import com.mangdehenzhi.enums.UserRole;
import com.mangdehenzhi.repository.AssessmentRepository;
import com.mangdehenzhi.repository.AssessmentResultRepository;
import com.mangdehenzhi.repository.CertificationRepository;
import com.mangdehenzhi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LearningAnalyticsServiceTest {

    @Autowired
    private LearningAnalyticsService analyticsService;

    @Autowired
    private AssessmentResultRepository assessmentResultRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Assessment testAssessment;

    @BeforeEach
    void setUp() {
        certificationRepository.deleteAll();
        assessmentResultRepository.deleteAll();
        assessmentRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(User.builder()
                .username("analytics_test").role(UserRole.STUDENT)
                .email("analytics@test.com").password("test")
                .build());
        testAssessment = Assessment.builder()
                .title("测试测评").difficulty(DifficultyLevel.ADAPTIVE)
                .duration(60).totalScore(100).passScore(60).status(AssessmentStatus.COMPLETED)
                .build();
        testAssessment = assessmentRepository.save(testAssessment);
    }

    @Test
    void getAnalytics_WithNoData_ShouldReturnDefaults() {
        var analytics = analyticsService.getAnalytics(testUser.getId());
        assertNotNull(analytics);
        assertEquals(0, analytics.totalAssessments());
        assertEquals(0.0, analytics.overallScore());
    }

    @Test
    void getAnalytics_WithAssessmentData_ShouldCalculateCorrectly() {
        AssessmentResult r1 = AssessmentResult.builder()
                .assessment(testAssessment).user(testUser).score(80).passed(true)
                .dimensionScores(Map.of("communication", 80, "collaboration", 70))
                .completedAt(LocalDateTime.now().minusDays(2))
                .build();
        AssessmentResult r2 = AssessmentResult.builder()
                .assessment(testAssessment).user(testUser).score(90).passed(true)
                .dimensionScores(Map.of("communication", 90, "collaboration", 85))
                .completedAt(LocalDateTime.now())
                .build();
        assessmentResultRepository.save(r1);
        assessmentResultRepository.save(r2);

        var analytics = analyticsService.getAnalytics(testUser.getId());
        assertEquals(2, analytics.totalAssessments());
        assertTrue(analytics.overallScore() > 0);
        assertFalse(analytics.skillTrends().isEmpty());
    }

    @Test
    void getAnalytics_ShouldDetectWeakAreas() {
        AssessmentResult r = AssessmentResult.builder()
                .assessment(testAssessment).user(testUser).score(50).passed(false)
                .dimensionScores(Map.of("communication", 45, "collaboration", 55))
                .completedAt(LocalDateTime.now())
                .build();
        assessmentResultRepository.save(r);

        var analytics = analyticsService.getAnalytics(testUser.getId());
        assertFalse(analytics.weakAreas().isEmpty());
        assertTrue(analytics.weakAreas().stream().allMatch(w -> w.score() < 70));
    }

    @Test
    void getAnalytics_ShouldGenerateRecommendations() {
        AssessmentResult r = AssessmentResult.builder()
                .assessment(testAssessment).user(testUser).score(60).passed(true)
                .dimensionScores(Map.of("communication", 50))
                .completedAt(LocalDateTime.now())
                .build();
        assessmentResultRepository.save(r);

        var analytics = analyticsService.getAnalytics(testUser.getId());
        assertFalse(analytics.recommendations().isEmpty());
        assertTrue(analytics.recommendations().stream().anyMatch(s -> s.contains("薄弱")));
    }
}