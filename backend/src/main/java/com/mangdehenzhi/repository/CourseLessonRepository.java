package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.CourseLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseLessonRepository extends JpaRepository<CourseLesson, Long> {
    List<CourseLesson> findByCourseIdOrderBySortOrderAsc(Long courseId);
    List<CourseLesson> findByCourseIdAndDifficulty(Long courseId, String difficulty);
}