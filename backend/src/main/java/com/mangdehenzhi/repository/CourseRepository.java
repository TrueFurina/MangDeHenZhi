package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.enums.CourseCategory;
import com.mangdehenzhi.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategory(CourseCategory category);
    List<Course> findByDifficulty(DifficultyLevel difficulty);
    List<Course> findByPublishedTrue();
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByTitleContainingIgnoreCase(String title);
}