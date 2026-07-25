package com.mangdehenzhi.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 业务监控指标 — 记录关键业务事件的计数和执行耗时
 * 通过 Actuator + Prometheus 暴露，用于 Grafana 监控面板
 */
@Slf4j
@Component
public class BusinessMetrics {

    private final MeterRegistry meterRegistry;

    // 计数器
    private final Counter userRegistrations;
    private final Counter assessmentsSubmitted;
    private final Counter certificatesIssued;
    private final Counter loginAttempts;
    private final Counter apiErrors;

    // 业务操作耗时
    private final Timer assessmentTimer;
    private final Timer loginTimer;

    // 自定义标签追踪
    private final ConcurrentHashMap<String, Counter> customCounters = new ConcurrentHashMap<>();

    public BusinessMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.userRegistrations = Counter.builder("mangdehenzhi.user.registrations")
                .description("Total user registrations")
                .register(meterRegistry);

        this.assessmentsSubmitted = Counter.builder("mangdehenzhi.assessment.submissions")
                .description("Total assessment submissions")
                .register(meterRegistry);

        this.certificatesIssued = Counter.builder("mangdehenzhi.certificate.issued")
                .description("Total certificates issued")
                .register(meterRegistry);

        this.loginAttempts = Counter.builder("mangdehenzhi.login.attempts")
                .description("Total login attempts")
                .register(meterRegistry);

        this.apiErrors = Counter.builder("mangdehenzhi.api.errors")
                .description("Total API errors (5xx)")
                .register(meterRegistry);

        this.assessmentTimer = Timer.builder("mangdehenzhi.assessment.duration")
                .description("Assessment submission duration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.loginTimer = Timer.builder("mangdehenzhi.login.duration")
                .description("Login request duration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    @PostConstruct
    public void init() {
        log.info("BusinessMetrics initialized — {} counters and {} timers registered",
                meterRegistry.getMeters().size(), 2);
    }

    // ===== 计数器方法 =====

    public void incrementUserRegistrations() { userRegistrations.increment(); }
    public void incrementAssessmentsSubmitted() { assessmentsSubmitted.increment(); }
    public void incrementCertificatesIssued() { certificatesIssued.increment(); }
    public void incrementLoginAttempts() { loginAttempts.increment(); }
    public void incrementApiErrors() { apiErrors.increment(); }

    public void incrementCustom(String name, String... tags) {
        String key = name + ":" + String.join("_", tags);
        customCounters.computeIfAbsent(key, k ->
                Counter.builder("mangdehenzhi." + name)
                        .tags(tags)
                        .description("Custom metric: " + name)
                        .register(meterRegistry)
        ).increment();
    }

    // ===== 计时器方法 =====

    public Timer.Sample startAssessmentTimer() { return Timer.start(meterRegistry); }
    public void stopAssessmentTimer(Timer.Sample sample) { sample.stop(assessmentTimer); }

    public Timer.Sample startLoginTimer() { return Timer.start(meterRegistry); }
    public void stopLoginTimer(Timer.Sample sample) { sample.stop(loginTimer); }

    // ===== Gauge（实时值） =====

    public void recordOnlineUsers(int count) {
        meterRegistry.gauge("mangdehenzhi.users.online", count);
    }

    public void recordPendingAssessments(long count) {
        meterRegistry.gauge("mangdehenzhi.assessment.pending", count);
    }
}