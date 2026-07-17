package com.mangdehenzhi.service;

import com.mangdehenzhi.dto.LoginRequest;
import com.mangdehenzhi.dto.LoginResponse;
import com.mangdehenzhi.dto.RegisterRequest;
import com.mangdehenzhi.entity.User;
import com.mangdehenzhi.enums.UserRole;
import com.mangdehenzhi.exception.BusinessException;
import com.mangdehenzhi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_ShouldCreateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");
        request.setNickname("测试用户");

        LoginResponse response = userService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("testuser", response.getUser().getUsername());
        assertTrue(userRepository.existsByUsername("testuser"));
    }

    @Test
    void register_WithDuplicateUsername_ShouldThrow() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("duplicate");
        request.setPassword("password123");
        request.setEmail("first@example.com");
        userService.register(request);

        RegisterRequest duplicate = new RegisterRequest();
        duplicate.setUsername("duplicate");
        duplicate.setPassword("password123");
        duplicate.setEmail("second@example.com");

        assertThrows(BusinessException.class, () -> userService.register(duplicate));
    }

    @Test
    void register_WithDuplicateEmail_ShouldThrow() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");
        request.setEmail("same@example.com");
        userService.register(request);

        RegisterRequest duplicate = new RegisterRequest();
        duplicate.setUsername("user2");
        duplicate.setPassword("password123");
        duplicate.setEmail("same@example.com");

        assertThrows(BusinessException.class, () -> userService.register(duplicate));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        // 先注册
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("logintest");
        reg.setPassword("password123");
        reg.setEmail("login@example.com");
        userService.register(reg);

        // 登录
        LoginRequest login = new LoginRequest();
        login.setUsername("logintest");
        login.setPassword("password123");
        LoginResponse response = userService.login(login);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("logintest", response.getUser().getUsername());
    }

    @Test
    void login_WithWrongPassword_ShouldThrow() {
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("wrongpw");
        reg.setPassword("correctpass");
        reg.setEmail("wrongpw@example.com");
        userService.register(reg);

        LoginRequest login = new LoginRequest();
        login.setUsername("wrongpw");
        login.setPassword("wrongpass");

        assertThrows(BusinessException.class, () -> userService.login(login));
    }

    @Test
    void login_WithNonExistentUser_ShouldThrow() {
        LoginRequest login = new LoginRequest();
        login.setUsername("nobody");
        login.setPassword("anypass");

        assertThrows(BusinessException.class, () -> userService.login(login));
    }

    @Test
    void register_ShouldSetDefaultRoleAsStudent() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("studentrole");
        request.setPassword("password123");
        request.setEmail("studentrole@example.com");

        LoginResponse response = userService.register(request);
        assertEquals(UserRole.STUDENT, response.getUser().getRole());
    }
}