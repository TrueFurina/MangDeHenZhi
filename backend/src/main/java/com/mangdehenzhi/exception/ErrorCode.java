package com.mangdehenzhi.exception;

/**
 * 统一错误码 — 全系统共用
 * 格式: [模块]_[具体错误]，便于定位和监控
 */
public enum ErrorCode {
    // ===== 通用 (1xxx) =====
    SUCCESS(0, "成功"),
    INTERNAL_ERROR(1000, "服务器内部错误"),
    SERVICE_UNAVAILABLE(1001, "服务暂不可用"),
    RATE_LIMITED(1002, "请求过于频繁"),

    // ===== 认证 (2xxx) =====
    UNAUTHORIZED(2000, "未登录或Token已过期"),
    FORBIDDEN(2001, "没有权限执行此操作"),
    INVALID_TOKEN(2002, "Token无效"),
    TOKEN_EXPIRED(2003, "Token已过期"),
    INVALID_CREDENTIALS(2004, "用户名或密码错误"),
    ACCOUNT_DISABLED(2005, "账号已被禁用"),
    CAPTCHA_ERROR(2006, "验证码错误"),

    // ===== 用户 (3xxx) =====
    USER_NOT_FOUND(3000, "用户不存在"),
    USERNAME_EXISTS(3001, "用户名已存在"),
    EMAIL_EXISTS(3002, "邮箱已被注册"),
    USERNAME_INVALID(3003, "用户名只能包含字母、数字和下划线"),
    PASSWORD_WEAK(3004, "密码必须包含字母和数字"),

    // ===== 测评 (4xxx) =====
    ASSESSMENT_NOT_FOUND(4000, "测评不存在"),
    ASSESSMENT_ALREADY_COMPLETED(4001, "测评已完成"),
    INVALID_DIMENSION_SCORE(4002, "维度得分必须在0-100之间"),
    ASSESSMENT_EXPIRED(4003, "测评已过期"),

    // ===== 课程 (5xxx) =====
    COURSE_NOT_FOUND(5000, "课程不存在"),
    COURSE_NOT_PUBLISHED(5001, "课程未发布"),

    // ===== 证书 (6xxx) =====
    CERTIFICATE_NOT_FOUND(6000, "证书不存在"),
    CERTIFICATE_ALREADY_ISSUED(6001, "证书已签发"),
    CERTIFICATE_VERIFICATION_FAILED(6002, "证书验证失败"),

    // ===== 元宇宙 (7xxx) =====
    SESSION_NOT_FOUND(7000, "会话不存在"),
    INVALID_SCENE_TYPE(7001, "无效的场景类型"),

    // ===== 数据库 (8xxx) =====
    DATA_INTEGRITY_VIOLATION(8000, "数据完整性冲突"),
    DUPLICATE_KEY(8001, "数据重复"),
    OPTIMISTIC_LOCK(8002, "数据已被修改，请刷新后重试");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}