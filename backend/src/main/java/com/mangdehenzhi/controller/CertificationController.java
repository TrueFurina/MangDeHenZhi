package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.entity.Certification;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Certification>>> getMyCertifications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(
                certificationService.getUserCertifications(user.getId())));
    }

    @GetMapping("/verify/{certHash}")
    public ResponseEntity<ApiResponse<Certification>> verifyCertification(
            @PathVariable String certHash) {
        Certification cert = certificationService.verifyCertification(certHash);
        return ResponseEntity.ok(ApiResponse.success("证书验证成功", cert));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Certification>> getCertification(@PathVariable Long id) {
        return certificationService.getCertificationById(id)
                .map(cert -> ResponseEntity.ok(ApiResponse.success(cert)))
                .orElseThrow(() -> new ResourceNotFoundException("Certification", id));
    }
}