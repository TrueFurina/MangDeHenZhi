package com.mangdehenzhi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {

    @Value("${jwt.secret:${JWT_SECRET:}}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final Set<String> KNOWN_BAD_SECRETS = Set.of(
            "local-dev-only-secret-do-not-use-in-prod-please-override",
            "mangdehenzhi-jwt-secret-key-2026-very-long-secret-for-security");

    @PostConstruct
    public void validateSecret() {
        if (secret == null || secret.isBlank()
                || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                "JWT secret 长度必须 >= 32 字节，且禁止在生产使用默认值。"
                + "请通过环境变量 JWT_SECRET 注入强随机密钥（如：openssl rand -base64 48）。");
        }
        boolean isProd = activeProfiles != null && activeProfiles.contains("prod");
        if (isProd) {
            // 生产环境：纵深防御，严禁使用已知占位/泄露密钥
            if (KNOWN_BAD_SECRETS.contains(secret)
                    || secret.startsWith("local-dev-only")
                    || secret.startsWith("mangdehenzhi-jwt")) {
                throw new IllegalStateException(
                    "生产环境检测到已知占位或已泄露的 JWT 密钥，禁止启动。"
                    + "请通过环境变量 JWT_SECRET 注入全新的强随机密钥（openssl rand -base64 48）。");
            }
        } else {
            // 非生产（dev/default）：允许本地占位密钥以开箱即用地本地开发/演示；
            // 但仍拒绝历史上已泄露的旧密钥，避免误用。
            if ("mangdehenzhi-jwt-secret-key-2026-very-long-secret-for-security".equals(secret)) {
                throw new IllegalStateException(
                    "已泄露的旧 JWT 密钥，禁止继续使用，请更换为全新密钥。");
            }
        }
    }

    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
}