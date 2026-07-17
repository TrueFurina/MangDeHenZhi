package com.mangdehenzhi.metaverse;

import lombok.Data;
import java.util.Map;

/**
 * 3D场景配置 - 供Three.js前端渲染使用
 */
@Data
public class SceneConfig {
    private String sceneId;
    private String sceneType;       // interview_room / classroom / meeting_room
    private Map<String, Object> environment; // 环境配置（光照、背景、音效）
    private Map<String, Object> characters;  // AI角色配置
    private Map<String, Object> interactions; // 交互配置
}