package com.mangdehenzhi.metaverse;

import lombok.Data;

/**
 * 元宇宙场景中的AI角色模型
 */
@Data
public class AICharacter {
    private String name;
    private String role;        // 面试官/同事/讲师/培训师
    private String behavior;    // 行为模式
    private String difficulty;  // 难度级别
    private String avatar;      // 3D模型路径
}