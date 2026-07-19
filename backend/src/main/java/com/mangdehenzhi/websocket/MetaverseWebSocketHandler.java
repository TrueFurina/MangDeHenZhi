package com.mangdehenzhi.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 元宇宙场景 WebSocket 处理器
 * 管理房间内的实时通信（位置同步、消息广播、事件通知）
 */
@Slf4j
@Component
public class MetaverseWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 房间 -> 会话集合
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    // 会话 -> 用户信息
    private final Map<WebSocketSession, UserInfo> sessionUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket 连接建立: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode json = objectMapper.readTree(message.getPayload());
        String type = json.path("type").asText();

        switch (type) {
            case "join" -> handleJoin(session, json);
            case "leave" -> handleLeave(session);
            case "move" -> handleMove(session, json);
            case "chat" -> handleChat(session, json);
            case "signal" -> handleSignal(session, json); // WebRTC 信令
            default -> log.warn("未知消息类型: {}", type);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        handleLeave(session);
        log.info("WebSocket 连接关闭: {}, 状态: {}", session.getId(), status);
    }

    // ===== 消息处理 =====

    private void handleJoin(WebSocketSession session, JsonNode json) {
        String roomId = json.path("roomId").asText();
        String userId = json.path("userId").asText();
        String username = json.path("username").asText();

        rooms.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        sessionUsers.put(session, new UserInfo(roomId, userId, username));

        // 广播加入通知
        broadcastToRoom(roomId, createMessage("user_joined", Map.of(
                "userId", userId, "username", username
        )), session);

        // 发送当前房间在线用户列表
        sendToSession(session, createMessage("room_info", Map.of(
                "roomId", roomId,
                "userCount", rooms.getOrDefault(roomId, Set.of()).size()
        )));

        log.info("用户 {} 加入房间 {}", username, roomId);
    }

    private void handleLeave(WebSocketSession session) {
        UserInfo info = sessionUsers.remove(session);
        if (info != null) {
            Set<WebSocketSession> roomSessions = rooms.get(info.roomId);
            if (roomSessions != null) {
                roomSessions.remove(session);
                if (roomSessions.isEmpty()) {
                    rooms.remove(info.roomId);
                }
            }
            broadcastToRoom(info.roomId, createMessage("user_left", Map.of(
                    "userId", info.userId, "username", info.username
            )), null);
            log.info("用户 {} 离开房间 {}", info.username, info.roomId);
        }
    }

    private void handleMove(WebSocketSession session, JsonNode json) {
        UserInfo info = sessionUsers.get(session);
        if (info == null) return;
        // 广播位置同步
        broadcastToRoom(info.roomId, createMessage("user_move", Map.of(
                "userId", info.userId,
                "position", json.path("position"),
                "rotation", json.path("rotation")
        )), session);
    }

    private void handleChat(WebSocketSession session, JsonNode json) {
        UserInfo info = sessionUsers.get(session);
        if (info == null) return;
        broadcastToRoom(info.roomId, createMessage("chat", Map.of(
                "userId", info.userId,
                "username", info.username,
                "message", json.path("message").asText(),
                "timestamp", System.currentTimeMillis()
        )), null);
    }

    private void handleSignal(WebSocketSession session, JsonNode json) {
        UserInfo info = sessionUsers.get(session);
        if (info == null) return;
        // WebRTC 信令转发（用于音视频通话）
        broadcastToRoom(info.roomId, createMessage("signal", Map.of(
                "fromUserId", info.userId,
                "fromUsername", info.username,
                "signalData", json.path("signalData")
        )), session);
    }

    // ===== 工具方法 =====

    private JsonNode createMessage(String type, Map<String, Object> data) {
        ObjectNode msg = objectMapper.createObjectNode();
        msg.put("type", type);
        data.forEach((k, v) -> {
            if (v instanceof String s) msg.put(k, s);
            else if (v instanceof Number n) msg.put(k, n.longValue());
            else if (v instanceof JsonNode j) msg.set(k, j);
            else msg.putPOJO(k, v);
        });
        return msg;
    }

    private void broadcastToRoom(String roomId, JsonNode message, WebSocketSession exclude) {
        Set<WebSocketSession> sessions = rooms.get(roomId);
        if (sessions == null) return;
        String text = message.toString();
        for (WebSocketSession s : sessions) {
            if (s.isOpen() && !s.equals(exclude)) {
                try {
                    s.sendMessage(new TextMessage(text));
                } catch (IOException e) {
                    log.error("广播消息失败: {}", e.getMessage());
                }
            }
        }
    }

    private void sendToSession(WebSocketSession session, JsonNode message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message.toString()));
            } catch (IOException e) {
                log.error("发送消息失败: {}", e.getMessage());
            }
        }
    }

    private record UserInfo(String roomId, String userId, String username) {}
}