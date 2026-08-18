package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    List<UserProgress> findByUserIdAndStatus(Long userId, String status);
    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
    long countByUserIdAndStatus(Long userId, String status);
    long countByLessonId(Long lessonId);
}