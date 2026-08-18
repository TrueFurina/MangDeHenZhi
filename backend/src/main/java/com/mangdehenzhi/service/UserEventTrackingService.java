package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.UserEvent;
import com.mangdehenzhi.repository.UserEventRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户行为追踪服务 — 专利级数据埋点
 * 记录用户关键行为事件，支持学习分析、行为预测和个性化推荐
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventTrackingService {

    private final UserEventRepository eventRepository;

    /**
     * 记录用户事件
     */
    public UserEvent trackEvent(Long userId, String eventType, String eventName,
                                Map<String, Object> eventData, HttpServletRequest request) {
        UserEvent event = UserEvent.builder()
                .userId(userId)
                .eventType(eventType)
                .eventName(eventName)
                .eventData(eventData != null ? eventData.toString() : null)
                .pageUrl(request != null ? request.getRequestURI() : null)
                .sessionId(request != null ? request.getSession().getId() : null)
                .ipAddress(request != null ? getClientIp(request) : null)
                .userAgent(request != null ? request.getHeader("User-Agent") : null)
                .build();
        return eventRepository.save(event);
    }

    /**
     * 获取用户事件历史
     */
    public List<UserEvent> getUserEvents(Long userId) {
        return eventRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 获取用户特定类型事件
     */
    public List<UserEvent> getUserEventsByType(Long userId, String eventType) {
        return eventRepository.findByUserIdAndEventTypeOrderByCreatedAtDesc(userId, eventType);
    }

    /**
     * 统计用户事件数量
     */
    public long countUserEvents(Long userId, String eventType) {
        return eventRepository.countByUserIdAndEventType(userId, eventType);
    }

    /**
     * 获取活跃用户统计
     */
    public Map<String, Long> getActivityStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime weekAgo = today.minusDays(7);

        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("today_logins", eventRepository.countByEventTypeAndCreatedAtBetween("login", today, now));
        stats.put("weekly_assessments", eventRepository.countByEventTypeAndCreatedAtBetween("assessment_complete", weekAgo, now));
        stats.put("weekly_lessons", eventRepository.countByEventTypeAndCreatedAtBetween("lesson_complete", weekAgo, now));
        stats.put("weekly_ai_chats", eventRepository.countByEventTypeAndCreatedAtBetween("ai_chat", weekAgo, now));
        return stats;
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank() && !"unknown".equalsIgnoreCase(xf)) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}