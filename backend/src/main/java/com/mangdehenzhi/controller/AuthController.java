package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.LoginRequest;
import com.mangdehenzhi.dto.LoginResponse;
import com.mangdehenzhi.dto.RegisterRequest;
import com.mangdehenzhi.service.CaptchaService;
import com.mangdehenzhi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CaptchaService captchaService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        validateCaptcha(request.getCaptchaKey(), request.getCaptchaAnswer());
        LoginResponse response = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        validateCaptcha(request.getCaptchaKey(), request.getCaptchaAnswer());
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }

    private void validateCaptcha(String key, Integer answer) {
        if (key != null && answer != null) {
            if (!captchaService.validate(key, answer)) {
                throw new com.mangdehenzhi.exception.BusinessException(400, "验证码错误");
            }
        }
        // 验证码为空时放行（兼容旧客户端，后续强制要求）
    }
}