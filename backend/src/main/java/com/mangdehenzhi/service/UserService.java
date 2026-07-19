package com.mangdehenzhi.service;

import com.mangdehenzhi.dto.LoginRequest;
import com.mangdehenzhi.dto.LoginResponse;
import com.mangdehenzhi.dto.RegisterRequest;
import com.mangdehenzhi.dto.UpdateProfileRequest;
import com.mangdehenzhi.dto.UserDTO;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.UserRole;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.repository.UserRepository;
import com.mangdehenzhi.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .phone(request.getPhone())
                .role(UserRole.STUDENT)
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new LoginResponse(token, UserDTO.fromEntity(user));
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(401, "用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (!user.getEnabled()) {
            throw new BusinessException(403, "账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(token, UserDTO.fromEntity(user));
    }

    public UserDTO getUserById(Long id, User currentUser) {
        // F-006：仅本人或 ADMIN 可查询用户，防止枚举全站 PII
        if (!currentUser.getId().equals(id) && currentUser.getRole() != UserRole.ADMIN) {
            throw new BusinessException(403, "无权访问该用户");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return UserDTO.fromEntity(user);
    }

    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    public UserDTO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getCurrentUser(userId);
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        user = userRepository.save(user);
        return UserDTO.fromEntity(user);
    }

    // ===== Admin 方法 =====

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    public User updateUserRole(Long id, UserRole role) {
        User user = getUserEntityById(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    public User toggleUserStatus(Long id) {
        User user = getUserEntityById(id);
        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }

    public long getUserCount() {
        return userRepository.count();
    }
}