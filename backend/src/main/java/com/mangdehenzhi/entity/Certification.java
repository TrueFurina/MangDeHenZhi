package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.CertificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certifications", indexes = {
    @Index(name = "idx_cert_hash", columnList = "certHash", unique = true),
    @Index(name = "idx_cert_user", columnList = "user_id"),
    @Index(name = "idx_cert_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String certHash; // 证书哈希

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_result_id")
    private AssessmentResult assessmentResult;

    @Column(nullable = false, length = 200)
    private String title; // 证书名称

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String blockchainTxId; // 区块链交易ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CertificationStatus status = CertificationStatus.ISSUED;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime verifiedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}