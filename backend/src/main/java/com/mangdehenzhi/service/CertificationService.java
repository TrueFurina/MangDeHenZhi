package com.mangdehenzhi.service;

import com.mangdehenzhi.blockchain.BlockchainService;
import com.mangdehenzhi.entity.AssessmentResult;
import com.mangdehenzhi.entity.Certification;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.CertificationStatus;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final BlockchainService blockchainService;

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

        // 区块链存证：将证书哈希上链，记录交易ID
        try {
            String txId = blockchainService.storeOnChain(certHash);
            certification.setBlockchainTxId(txId);
            log.info("证书 {} 已上链存证，交易ID: {}", certHash, txId);
        } catch (Exception e) {
            // 上链失败不影响证书签发，仅记录告警
            log.warn("证书上链失败（不影响签发）: {}", e.getMessage());
        }

        return certificationRepository.save(certification);
    }

    public Certification verifyCertification(String certHash) {
        Certification certification = certificationRepository.findByCertHash(certHash)
                .orElseThrow(() -> new ResourceNotFoundException("Certification", "certHash", certHash));

        // 链上验证（记录验证信息，不影响本地验证结果）
        try {
            String onChainResult = blockchainService.verifyOnChain(certHash);
            log.info("链上验证结果: {}", onChainResult);
        } catch (Exception e) {
            log.warn("链上验证异常（继续本地验证）: {}", e.getMessage());
        }

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