package com.mangdehenzhi.blockchain;

import com.mangdehenzhi.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * 平台签名服务：对证书/存证数据做 SHA256withRSA 签名与验真。
 *
 * 设计要点（对应 North Star M1 证书防伪 P0）：
 * - 生产环境（profile 含 "prod"）必须注入 CERT_SIGN_PRIVATE_KEY（PKCS#8 PEM），否则 fail-fast 拒绝启动。
 * - 未配置私钥时（本地/测试）生成临时 ephemeral 密钥，仅用于演示，重启即失效，禁止用于生产。
 * - verify 为只读、幂等，可区分平台签发的真实证书与伪造证书。
 */
public class CertSignatureService {

    private static final Logger log = LoggerFactory.getLogger(CertSignatureService.class);
    private static final String KEY_ALGO = "RSA";
    private static final String SIGN_ALGO = "SHA256withRSA";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final boolean ephemeral;

    public CertSignatureService(String privateKeyPem, String activeProfile) {
        if (privateKeyPem == null || privateKeyPem.isBlank()) {
            if (activeProfile != null && activeProfile.contains("prod")) {
                throw new IllegalStateException(
                        "生产环境必须配置平台签名私钥 CERT_SIGN_PRIVATE_KEY（PKCS#8 PEM），缺失则拒绝启动（fail-fast）。"
                                + "请通过环境变量注入，命令：openssl genrsa -out cert_sign.key 2048");
            }
            KeyPair kp = generateEphemeralKeyPair();
            this.privateKey = kp.getPrivate();
            this.publicKey = kp.getPublic();
            this.ephemeral = true;
            log.warn("未配置 CERT_SIGN_PRIVATE_KEY，已生成临时演示签名密钥（仅限本地/测试，重启后失效，禁止用于生产环境）。");
        } else {
            this.privateKey = parsePrivateKey(privateKeyPem);
            this.publicKey = derivePublicKey(this.privateKey);
            this.ephemeral = false;
        }
    }

    /** 对明文数据签名，返回 Base64 编码的签名串。 */
    public String sign(String data) {
        try {
            Signature sig = Signature.getInstance(SIGN_ALGO);
            sig.initSign(privateKey);
            sig.update(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            throw new BusinessException("签名失败: " + e.getMessage());
        }
    }

    /** 验证签名。空签名或异常一律返回 false（只读、幂等、可区分真假）。 */
    public boolean verify(String data, String signature) {
        if (signature == null || signature.isBlank()) {
            return false;
        }
        try {
            Signature sig = Signature.getInstance(SIGN_ALGO);
            sig.initVerify(publicKey);
            sig.update(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return sig.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    private PrivateKey parsePrivateKey(String pem) {
        try {
            String b64 = pem
                    .replaceAll("-----BEGIN (?:RSA )?PRIVATE KEY-----", "")
                    .replaceAll("-----END (?:RSA )?PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] raw = Base64.getDecoder().decode(b64);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(raw);
            return KeyFactory.getInstance(KEY_ALGO).generatePrivate(spec);
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            throw new BusinessException("私钥解析失败: " + e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("私钥解析失败: " + e.getMessage());
        }
    }

    private PublicKey derivePublicKey(PrivateKey privateKey) {
        if (privateKey instanceof RSAPrivateCrtKey crt) {
            try {
                RSAPublicKeySpec pub = new RSAPublicKeySpec(crt.getModulus(), crt.getPublicExponent());
                return KeyFactory.getInstance(KEY_ALGO).generatePublic(pub);
            } catch (Exception e) {
                throw new BusinessException("公钥派生失败: " + e.getMessage());
            }
        }
        throw new BusinessException("不支持的私钥类型，仅支持 RSA");
    }

    private KeyPair generateEphemeralKeyPair() {
        try {
            KeyPairGenerator g = KeyPairGenerator.getInstance(KEY_ALGO);
            g.initialize(2048);
            return g.generateKeyPair();
        } catch (Exception e) {
            throw new BusinessException("临时密钥生成失败: " + e.getMessage());
        }
    }
}
