package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用户实体的 JSON 契约守卫。
 *
 * <p>为什么单独存在：{@link User#getPassword()} 上的
 * {@code @JsonProperty(access = WRITE_ONLY)} 是**唯一阻止密码被序列化出去**的机制，
 * 而它依赖 Jackson 注解与 databind 的配合。这类属性在两个场景下会静默失效、且单测不报错：
 *
 * <ul>
 *   <li>Jackson 大版本升级（项目刚经历 Jackson 2 → Jackson 3，注解仍来自
 *       {@code jackson-annotations} 2.21，被 {@code tools.jackson.databind} 读取）；</li>
 *   <li>有人给某个接口单独配置了新的 {@code ObjectMapper} / 混用两套 JSON 库。</li>
 * </ul>
 *
 * <p>一旦失效，任何返回 User 的接口（登录、个人信息、用户列表）都会把明文密码吐给前端。
 * 这种回归靠人工 review 很难发现，故用测试钉住。
 *
 * <p>用 {@code @JsonTest} 而非 {@code @SpringBootTest}：只启动 JSON 切片，
 * 拿到的是**应用真实装配的 ObjectMapper**，而不是手搓一个 —— 后者测不出配置层面的问题。
 */
@JsonTest
class UserJsonSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("序列化 User 时不得输出 password（WRITE_ONLY 生效）")
    void passwordIsNeverSerialized() {
        User user = User.builder()
                .id(1L)
                .username("alice")
                .password("P@ssw0rd-must-not-leak")
                .email("alice@example.com")
                .nickname("Alice")
                .role(UserRole.STUDENT)
                .build();

        String json = objectMapper.writeValueAsString(user);

        // 关键断言：字段名与值都不能出现
        assertThat(json).doesNotContain("password");
        assertThat(json).doesNotContain("P@ssw0rd-must-not-leak");
        // 反向确认「不是整个对象被序列化成空」——其余字段必须正常输出
        assertThat(json).contains("alice");
        assertThat(json).contains("alice@example.com");
    }

    @Test
    @DisplayName("反序列化仍可写入 password（WRITE_ONLY 只挡输出，不影响输入）")
    void passwordIsStillDeserializable() {
        String payload = "{\"username\":\"bob\",\"password\":\"inbound-ok\","
                + "\"email\":\"bob@example.com\",\"role\":\"STUDENT\"}";

        User user = objectMapper.readValue(payload, User.class);

        assertThat(user.getUsername()).isEqualTo("bob");
        assertThat(user.getPassword()).isEqualTo("inbound-ok");
    }

    @Test
    @DisplayName("LocalDateTime 按 ISO-8601 输出且可逆（依赖 Jackson 3 内建的 java.time 支持）")
    void localDateTimeIsIsoAndReversible() {
        LocalDateTime t = LocalDateTime.of(2026, 10, 17, 16, 30, 0);
        User user = User.builder()
                .username("carol")
                .password("x")
                .email("carol@example.com")
                .role(UserRole.STUDENT)
                .createdAt(t)
                .build();

        String json = objectMapper.writeValueAsString(user);
        // 断言 ISO-8601 字面量（不要用 t.toString() 拼串：LocalDateTime.toString()
        // 在秒为 0 时会省略 ":00"，拼出来的期望值会与实际不符）
        assertThat(objectMapper.readTree(json).get("createdAt").asString())
                .isEqualTo("2026-10-17T16:30:00");

        User back = objectMapper.readValue(json, User.class);
        assertThat(back.getCreatedAt()).isEqualTo(t);
    }

    @Test
    @DisplayName("Hibernate 代理字段被忽略，反序列化不会因未知属性失败")
    void hibernateProxyFieldsAreIgnored() {
        String payload = "{\"username\":\"dave\",\"email\":\"dave@example.com\","
                + "\"role\":\"STUDENT\",\"hibernateLazyInitializer\":{},\"handler\":{}}";

        User user = objectMapper.readValue(payload, User.class);

        assertThat(user.getUsername()).isEqualTo("dave");
    }
}
