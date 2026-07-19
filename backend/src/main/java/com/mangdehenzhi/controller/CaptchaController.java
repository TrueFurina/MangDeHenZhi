package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping
    public ResponseEntity<ApiResponse<CaptchaService.CaptchaResult>> getCaptcha() {
        String key = UUID.randomUUID().toString().substring(0, 8);
        return ResponseEntity.ok(ApiResponse.success(captchaService.generate(key)));
    }
}