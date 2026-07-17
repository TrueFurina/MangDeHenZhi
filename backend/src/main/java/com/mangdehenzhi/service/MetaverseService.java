package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.MetaverseSession;
import com.mangdehenzhi.enums.MetaverseSceneType;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.repository.MetaverseSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 元宇宙场景服务 - 3D虚拟面试/培训场景管理
 */
@Service
@RequiredArgsConstructor
public class MetaverseService {

    private final MetaverseSessionRepository sessionRepository;

    @Transactional
    public MetaverseSession createSession(Long userId, String name, String sceneType, String config) {
        MetaverseSceneType type;
        try {
            type = MetaverseSceneType.valueOf(sceneType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("无效的场景类型: " + sceneType);
        }

        MetaverseSession session = MetaverseSession.builder()
                .user(com.mangdehenzhi.entity.User.builder().id(userId).build())
                .sessionName(name)
                .sceneType(type)
                .sceneConfig(config)
                .roomId(generateRoomId())
                .active(true)
                .startTime(LocalDateTime.now())
                .build();

        return sessionRepository.save(session);
    }

    @Transactional
    public MetaverseSession endSession(Long sessionId) {
        MetaverseSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("会话不存在"));

        session.setActive(false);
        session.setEndTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public List<MetaverseSession> getUserSessions(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public MetaverseSession getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("会话不存在"));
    }

    /**
     * 获取场景配置 - 供Three.js前端使用
     */
    public String getSceneConfig(MetaverseSceneType type) {
        return switch (type) {
            case INTERVIEW_ROOM -> """
                {
                    "scene": "interview_room",
                    "aiCharacters": ["面试官", "同事1", "同事2"],
                    "aiBehavior": {"responseTime": "realistic", "difficultyLevel": "adaptive"},
                    "features": ["voiceInteraction", "behaviorTracking", "realTimeFeedback"]
                }
                """;
            case CLASSROOM -> """
                {
                    "scene": "classroom",
                    "aiCharacters": ["讲师"],
                    "features": ["screenSharing", "whiteboard", "qaSession"]
                }
                """;
            case MEETING_ROOM -> """
                {
                    "scene": "meeting_room",
                    "features": ["screenSharing", "collaborativeEditing", "recording"]
                }
                """;
            case TRAINING_ROOM -> """
                {
                    "scene": "training_room",
                    "aiCharacters": ["培训师", "助教"],
                    "features": ["simulation", "realTimeFeedback", "progressTracking"]
                }
                """;
        };
    }

    private String generateRoomId() {
        return "room-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}