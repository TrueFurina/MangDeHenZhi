package com.mangdehenzhi.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * 链路追踪过滤器 — 为每个请求分配唯一 Trace ID
 * 
 * - 从请求头 X-Trace-Id 读取（上游传入），不存在则自动生成
 * - 注入 MDC 供日志框架使用（%X{traceId}）
 * - 在响应头 X-Trace-Id 返回，便于客户端关联
 */
@Component
@Order(1)
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String MDC_KEY = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 读取或生成 Trace ID
        String traceId = req.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = generateTraceId();
        }

        // 注入 MDC（日志框架自动输出）
        MDC.put(MDC_KEY, traceId);

        // 响应头返回（客户端可关联请求与日志）
        res.setHeader(TRACE_ID_HEADER, traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            // 请求结束后清除 MDC，防止线程池复用导致串号
            MDC.remove(MDC_KEY);
        }
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}