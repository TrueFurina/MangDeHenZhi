package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.Assessment;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.Certification;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.AssessmentStatus;
import com.mangdehenzhi.enums.CertificationStatus;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CertificationServiceTest {

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentResultRepository resultRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private AssessmentResult testResult;

    @BeforeEach
    void setUp() {
        certificationRepository.deleteAll();
        resultRepository.deleteAll();
        assessmentRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .username("certuser")
                .password(passwordEncoder.encode("test123"))
                .email("cert@example.com")
                .role(UserRole.STUDENT)
                .build());

        Assessment assessment = assessmentRepository.save(Assessment.builder()
                .title("综合技能测评")
                .difficulty(DifficultyLevel.ADAPTIVE)
                .duration(60)
                .totalScore(300)
                .passScore(180)
                .status(AssessmentStatus.COMPLETED)
                .build());

        testResult = resultRepository.save(AssessmentResult.builder()
                .assessment(assessment)
                .user(testUser)
                .score(240)
                .passed(true)
                .dimensionScores(Map.of("communication", 80, "collaboration", 70, "problem_solving", 90))
                .completedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void issueCertification_ShouldCreateCertificate() {
        Certification cert = certificationService.issueCertification(testUser, testResult);

        assertNotNull(cert);
        assertNotNull(cert.getCertHash());
        assertEquals(testUser.getId(), cert.getUser().getId());
        assertEquals(CertificationStatus.ISSUED, cert.getStatus());
        assertTrue(cert.getTitle().contains("综合技能测评"));
    }

    @Test
    void issueCertification_ShouldGenerateUniqueHashes() {
        Certification cert1 = certificationService.issueCertification(testUser, testResult);
        Certification cert2 = certificationService.issueCertification(testUser, testResult);

        assertNotEquals(cert1.getCertHash(), cert2.getCertHash());
    }

    @Test
    void verifyCertification_ShouldUpdateStatus() {
        Certification cert = certificationService.issueCertification(testUser, testResult);

        Certification verified = certificationService.verifyCertification(cert.getCertHash());

        assertEquals(CertificationStatus.VERIFIED, verified.getStatus());
        assertNotNull(verified.getVerifiedAt());
    }

    @Test
    void getUserCertifications_ShouldReturnUserCerts() {
        certificationService.issueCertification(testUser, testResult);

        List<Certification> certs = certificationService.getUserCertifications(testUser.getId());
        assertEquals(1, certs.size());
    }
}