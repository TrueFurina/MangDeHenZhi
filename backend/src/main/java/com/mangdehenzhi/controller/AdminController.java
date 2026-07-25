package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.PageDTO;
import com.mangdehenzhi.dto.RegisterRequest;
import com.mangdehenzhi.dto.UserDTO;
import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.UserRole;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.service.CourseService;
import com.mangdehenzhi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 管理后台端点 — 仅 ADMIN 角色可访问
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;

    // ===== 用户管理 =====

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(@AuthenticationPrincipal User admin) {
        checkAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@AuthenticationPrincipal User admin, @PathVariable Long id) {
        checkAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success(userService.getUserEntityById(id)));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<User>> createUser(
            @AuthenticationPrincipal User admin,
            @Valid @RequestBody RegisterRequest request) {
        checkAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success("用户创建成功",
                userService.createUserByAdmin(request)));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id,
            @RequestBody RoleRequest request) {
        checkAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success(userService.updateUserRole(id, request.role())));
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id) {
        checkAdmin(admin);
        if (admin.getId().equals(id)) {
            throw new BusinessException("不能禁用自己");
        }
        return ResponseEntity.ok(ApiResponse.success(userService.toggleUserStatus(id)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id) {
        checkAdmin(admin);
        if (admin.getId().equals(id)) {
            throw new BusinessException("不能删除自己");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户已删除", null));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllRoles(
            @AuthenticationPrincipal User admin) {
        checkAdmin(admin);
        List<Map<String, Object>> roles = Arrays.stream(UserRole.values()).map(role -> {
            Map<String, Object> r = new HashMap<>();
            r.put("name", role.name());
            r.put("description", switch (role) {
                case ADMIN -> "管理员 — 全系统管理权限";
                case TEACHER -> "教师 — 课程管理与测评创建";
                case STUDENT -> "学员 — 学习和测评";
            });
            return r;
        }).toList();
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    // ===== 课程管理 =====

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<PageDTO<Course>>> getAllCourses(
            @AuthenticationPrincipal User admin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        checkAdmin(admin);
        var pageResult = courseService.getCoursesPage(page, size);
        return ResponseEntity.ok(ApiResponse.success(
                PageDTO.of(pageResult.getContent(), page, size, pageResult.getTotalElements())));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<Course>> createCourse(
            @AuthenticationPrincipal User admin,
            @RequestBody Course course) {
        checkAdmin(admin);
        course.setId(null);
        return ResponseEntity.ok(ApiResponse.success("课程创建成功", courseService.createCourse(course)));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id,
            @RequestBody Course courseData) {
        checkAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success(courseService.updateCourse(id, courseData)));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id) {
        checkAdmin(admin);
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("课程已删除", null));
    }

    // ===== 数据统计 =====

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<StatsResponse>> getStats(@AuthenticationPrincipal User admin) {
        checkAdmin(admin);
        long userCount = userService.getUserCount();
        long courseCount = courseService.getCoursesPage(0, 1).getTotalElements();
        return ResponseEntity.ok(ApiResponse.success(new StatsResponse(userCount, courseCount)));
    }

    // ===== 内部 =====

    private void checkAdmin(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }

    public record RoleRequest(UserRole role) {}
    public record StatsResponse(long userCount, long courseCount) {}
}