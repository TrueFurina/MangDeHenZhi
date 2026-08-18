package com.mangdehenzhi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 元宇宙 WebSocket 握手鉴权拦截器（S-001）。
 *
 * 在 HTTP 升级为 WebSocket 的握手阶段校验 JWT。浏览器原生 WebSocket 无法自定义请求头，
 * 故令牌通过查询参数 {@code ?token=<JWT>} 传递。校验通过后将权威身份（userId/username）
 * 写入会话属性，供 {@link com.mangdehenzhi.websocket.MetaverseWebSocketHandler} 使用，
 * 使消息处理阶段不再信任客户端自报的 userId/username，从根本上杜绝冒充任意用户。
 *
 * 任何缺少 / 无效令牌的握手均被拒绝。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    /** 握手阶段写入会话属性的权威身份键名，供 WS Handler 读取。 */
    public static final String ATTR_USER_ID = "wsUserId";
    public static final String ATTR_USERNAME = "wsUsername";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            log.warn("WS 握手被拒绝：非 Servlet 环境的请求");
            return false;
        }
        String token = servletRequest.getServletRequest().getParameter("token");
        if (token == null || token.isBlank()) {
            log.warn("WS 握手被拒绝：缺少 token 查询参数");
            return false;
        }
        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("WS 握手被拒绝：token 无效或已过期");
                return false;
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                log.warn("WS 握手被拒绝：token 缺少 userId 声明");
                return false;
            }
            String username = jwtUtil.parseToken(token).get("username", String.class);
            attributes.put(ATTR_USER_ID, userId);
            attributes.put(ATTR_USERNAME, (username != null && !username.isBlank()) ? username : ("user-" + userId));
            return true;
        } catch (Exception e) {
            log.warn("WS 握手被拒绝：token 解析失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {
        // 无需处理
    }
}
