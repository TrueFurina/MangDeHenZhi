package com.mangdehenzhi.config;

import com.mangdehenzhi.entity.*;
import com.mangdehenzhi.enums.*;
import com.mangdehenzhi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentResultRepository resultRepository;
    private final CertificationRepository certificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("数据库已有数据，跳过初始化");
            return;
        }

        log.info("========== 初始化测试数据 ==========");
        initUsers();
        initCourses();
        initAssessments();
        log.info("========== 测试数据初始化完成 ==========");
    }

    private void initUsers() {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@mangdehenzhi.com")
                .nickname("管理员")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin);

        User teacher = User.builder()
                .username("teacher")
                .password(passwordEncoder.encode("teacher123"))
                .email("teacher@mangdehenzhi.com")
                .nickname("张老师")
                .role(UserRole.TEACHER)
                .build();
        userRepository.save(teacher);

        User student = User.builder()
                .username("student")
                .password(passwordEncoder.encode("student123"))
                .email("student@mangdehenzhi.com")
                .nickname("李同学")
                .role(UserRole.STUDENT)
                .build();
        userRepository.save(student);

        log.info("✅ 用户数据初始化完成: admin/teacher/student");
    }

    private void initCourses() {
        List<Course> courses = List.of(
            Course.builder()
                .title("Java 企业级开发实战")
                .description("从零到一掌握Spring Boot + JPA + Security企业级开发")
                .category(CourseCategory.TECHNOLOGY)
                .difficulty(DifficultyLevel.INTERMEDIATE)
                .duration(2400)
                .price(new BigDecimal("299.00"))
                .published(true)
                .build(),
            Course.builder()
                .title("Web 前端进阶教程")
                .description("Vue 3 + TypeScript + Vite 现代前端开发")
                .category(CourseCategory.TECHNOLOGY)
                .difficulty(DifficultyLevel.INTERMEDIATE)
                .duration(1800)
                .price(new BigDecimal("199.00"))
                .published(true)
                .build(),
            Course.builder()
                .title("AI 与机器学习入门")
                .description("零基础入门人工智能，掌握机器学习核心概念")
                .category(CourseCategory.TECHNOLOGY)
                .difficulty(DifficultyLevel.BEGINNER)
                .duration(1200)
                .price(new BigDecimal("399.00"))
                .published(true)
                .build(),
            Course.builder()
                .title("沟通与协作技巧")
                .description("提升职场沟通能力，高效团队协作方法")
                .category(CourseCategory.SOFT_SKILLS)
                .difficulty(DifficultyLevel.BEGINNER)
                .duration(600)
                .price(new BigDecimal("99.00"))
                .published(true)
                .build(),
            Course.builder()
                .title("商业思维与创新")
                .description("培养商业思维，掌握创新方法论")
                .category(CourseCategory.BUSINESS)
                .difficulty(DifficultyLevel.INTERMEDIATE)
                .duration(900)
                .price(new BigDecimal("199.00"))
                .published(true)
                .build()
        );
        courseRepository.saveAll(courses);
        log.info("✅ 课程数据初始化完成: {} 门课程", courses.size());
    }

    private void initAssessments() {
        Assessment assessment = Assessment.builder()
                .title("综合技能测评")
                .description("从沟通能力、协作能力和问题解决能力三个维度进行全面评估")
                .difficulty(DifficultyLevel.ADAPTIVE)
                .dimensions(List.of("communication", "collaboration", "problem_solving"))
                .duration(60)
                .totalScore(300)
                .passScore(180)
                .status(AssessmentStatus.PENDING)
                .build();
        assessmentRepository.save(assessment);
        log.info("✅ 测评数据初始化完成");
    }
}