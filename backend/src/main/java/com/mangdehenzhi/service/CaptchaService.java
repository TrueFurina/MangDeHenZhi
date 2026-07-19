package com.mangdehenzhi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码服务 — 生成和校验数学验证码
 * 使用内存缓存，60秒过期
 */
@Slf4j
@Service
public class CaptchaService {

    private final Map<String, CaptchaEntry> store = new ConcurrentHashMap<>();

    private static final long EXPIRY_MS = 60_000L;

    public CaptchaResult generate(String key) {
        int a = ThreadLocalRandom.current().nextInt(10, 99);
        int b = ThreadLocalRandom.current().nextInt(1, 10);
        String op = ThreadLocalRandom.current().nextBoolean() ? "+" : "-";
        int answer = switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            default -> a + b;
        };
        String question = a + " " + op + " " + b + " = ?";
        store.put(key, new CaptchaEntry(answer, System.currentTimeMillis()));
        return new CaptchaResult(key, question);
    }

    public boolean validate(String key, int answer) {
        CaptchaEntry entry = store.remove(key);
        if (entry == null) return false;
        if (System.currentTimeMillis() - entry.timestamp > EXPIRY_MS) return false;
        return entry.answer == answer;
    }

    public record CaptchaResult(String key, String question) {}
    private record CaptchaEntry(int answer, long timestamp) {}
}