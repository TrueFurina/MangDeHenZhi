package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.Enrollment;
import com.mangdehenzhi.enums.EnrollmentStatus;
import com.mangdehenzhi.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Enrollment enroll(Long userId, Long courseId) {
        Enrollment e = Enrollment.builder()
                .userId(userId)
                .courseId(courseId)
                .status(EnrollmentStatus.ACTIVE)
                .enrolledAt(LocalDateTime.now())
                .build();
        return enrollmentRepository.save(e);
    }

    @Transactional
    public Enrollment complete(Long enrollmentId) {
        Enrollment e = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new com.mangdehenzhi.exception.ResourceNotFoundException("报名记录不存在"));
        e.setStatus(EnrollmentStatus.COMPLETED);
        e.setCompletedAt(LocalDateTime.now());
        return enrollmentRepository.save(e);
    }

    public List<Enrollment> listByUser(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    public List<Enrollment> listByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
}
