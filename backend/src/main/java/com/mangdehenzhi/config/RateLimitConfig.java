package com.mangdehenzhi.config;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * API 限流保护 — 基于 IP 的简单滑动窗口限流
 * 防止暴力破解和 DDoS 攻击
 */
@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    @Value("${rate-limit.max-requests:60}")
    private int maxRequests;

    @Value("${rate-limit.window-seconds:60}")
    private int windowSeconds;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(maxRequests, windowSeconds))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/health");
    }

    record RateLimitEntry(AtomicInteger count, long windowStart) {}

    static class RateLimitInterceptor implements HandlerInterceptor {

        private final Map<String, RateLimitEntry> cache = new ConcurrentHashMap<>();
        private final int maxRequests;
        private final long windowMillis;

        RateLimitInterceptor(int maxRequests, int windowSeconds) {
            this.maxRequests = maxRequests;
            this.windowMillis = windowSeconds * 1000L;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler) throws Exception {
            String ip = getClientIp(request);
            long now = System.currentTimeMillis();

            RateLimitEntry entry = cache.compute(ip, (key, existing) -> {
                if (existing == null || (now - existing.windowStart()) > windowMillis) {
                    return new RateLimitEntry(new AtomicInteger(1), now);
                }
                existing.count().incrementAndGet();
                return existing;
            });

            if (entry.count().get() > maxRequests) {
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        "{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\",\"data\":null}");
                return false;
            }

            return true;
        }

        private String getClientIp(HttpServletRequest request) {
            String xf = request.getHeader("X-Forwarded-For");
            if (xf != null && !xf.isBlank() && !"unknown".equalsIgnoreCase(xf)) {
                return xf.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        }
    }
}