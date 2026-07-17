package com.mangdehenzhi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.time.Instant;

/**
 * API 请求日志拦截器 — 记录每个请求的方法、路径、耗时和状态码
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private static final ThreadLocal<Instant> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(Instant.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) {
        Instant start = startTime.get();
        if (start == null) return;
        startTime.remove();

        long ms = Duration.between(start, Instant.now()).toMillis();
        int status = response.getStatus();

        // 慢请求告警（超过 3 秒）
        if (ms > 3000) {
            log.warn("⚡ SLOW  [{} {}] → {} ({}ms)", request.getMethod(), request.getRequestURI(), status, ms);
        } else {
            log.info("📡 API  [{} {}] → {} ({}ms)", request.getMethod(), request.getRequestURI(), status, ms);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/api/**");
    }
}