package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.Certification;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.CertificationStatus;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public Certification issueCertification(User user, AssessmentResult assessmentResult) {
        // 生成唯一证书哈希
        String certHash = generateCertHash(user.getId(), assessmentResult.getId());

        Certification certification = Certification.builder()
                .certHash(certHash)
                .user(user)
                .assessmentResult(assessmentResult)
                .title(assessmentResult.getAssessment().getTitle() + " - 技能认证")
                .description("通过" + assessmentResult.getAssessment().getTitle() + "测评，成绩：" + assessmentResult.getScore())
                .status(CertificationStatus.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();

        // 区块链存证（后续实现）
        // certification.setBlockchainTxId(blockchainService.storeOnChain(certHash));

        return certificationRepository.save(certification);
    }

    public Certification verifyCertification(String certHash) {
        Certification certification = certificationRepository.findByCertHash(certHash)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "certHash", certHash));

        certification.setVerifiedAt(LocalDateTime.now());
        certification.setStatus(CertificationStatus.VERIFIED);
        return certificationRepository.save(certification);
    }

    public List<Certification> getUserCertifications(Long userId) {
        return certificationRepository.findByUserId(userId);
    }

    public Optional<Certification> getCertificationById(Long id) {
        return certificationRepository.findById(id);
    }

    private String generateCertHash(Long userId, Long resultId) {
        String raw = userId + "-" + resultId + "-" + System.nanoTime() + "-" + java.util.UUID.randomUUID();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("证书哈希生成失败");
        }
    }
}