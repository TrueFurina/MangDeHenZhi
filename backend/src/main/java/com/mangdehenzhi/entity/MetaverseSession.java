package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.MetaverseSceneType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "metaverse_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
public class MetaverseSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MetaverseSceneType sceneType;

    @Column(length = 200)
    private String sessionName;

    @Column(columnDefinition = "TEXT")
    private String sceneConfig; // 场景配置（JSON）

    @Column(length = 100)
    private String roomId; // 房间ID

    @Builder.Default
    private Boolean active = false;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String interactionData; // 交互数据（JSON）

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}