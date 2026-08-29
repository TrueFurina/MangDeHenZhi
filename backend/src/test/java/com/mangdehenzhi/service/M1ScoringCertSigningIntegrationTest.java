package com.mangdehenzhi.service;

import com.mangdehenzhi.blockchain.CertSignatureService;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * M1 集成验证：服务端权威计分 + 平台签名证书防伪。
 * 不依赖 Spring 容器，直接校验两项核心能力，便于 CI 快速跑通。
 */
class M1ScoringCertSigningIntegrationTest {

    @Test
    void scoringBank_recomputesDimensionScoresServerSide() {
        AssessmentScoringBank bank = new AssessmentScoringBank();

        Map<String, Integer> answers = new LinkedHashMap<>();
        answers.put("comm-1", 4);
        answers.put("comm-2", 3);
        answers.put("collab-1", 4);
        answers.put("prob-1", 2);
        answers.put("lead-1", 1);
        answers.put("adapt-1", 4);

        Map<String, Integer> scores = bank.computeDimensionScores(answers);

        // communication: comm-1(90) + comm-2(65) = 155
        assertEquals(155, scores.get("communication"));
        // collaboration: collab-1(90)
        assertEquals(90, scores.get("collaboration"));
        // 未作答的维度计为 0
        assertEquals(0, scores.get("problem_solving"));
    }

    @Test
    void certSignature_roundTrip_isVerifiable() {
        // 未配置私钥 -> 临时演示密钥（非生产），可正常签名验签
        CertSignatureService svc = new CertSignatureService("", "dev");
        assertTrue(svc.isEphemeral());

        String payload = "cert:user-1:course-42:2026";
        String sig = svc.sign(payload);
        assertNotNull(sig);
        assertTrue(svc.verify(payload, sig));
        // 篡改后验签必须失败（防伪）
        assertFalse(svc.verify(payload + "-tampered", sig));
        assertFalse(svc.verify(payload, "not-a-real-sig"));
    }

    @Test
    void certSignature_prodRequiresKey() {
        // 生产环境缺私钥必须 fail-fast
        try {
            new CertSignatureService("", "prod");
            throw new AssertionError("生产环境缺私钥应拒绝启动");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("CERT_SIGN_PRIVATE_KEY"));
        }
    }
}
