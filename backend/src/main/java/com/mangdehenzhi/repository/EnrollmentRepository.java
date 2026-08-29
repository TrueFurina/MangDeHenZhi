package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Long userId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStatus(com.mangdehenzhi.enums.EnrollmentStatus status);
}
