package com.mangdehenzhi.service;

import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.enums.CourseCategory;
import com.mangdehenzhi.exception.ResourceNotFoundException;
import com.mangdehenzhi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllPublishedCourses() {
        return courseRepository.findByPublishedTrue();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    public List<Course> getCoursesByCategory(CourseCategory category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> searchCourses(String query, int limit) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        List<Course> results = courseRepository.findByTitleContainingIgnoreCase(query.trim());
        if (results.size() > limit) {
            results = results.subList(0, limit);
        }
        return results;
    }

    public Page<Course> getCoursesPage(int page, int size) {
        return courseRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Page<Course> getCoursesPage(int page, int size, String sortBy, String order) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String field = List.of("title", "price", "duration", "createdAt", "enrollmentCount").contains(sortBy)
                ? sortBy : "createdAt";
        return courseRepository.findAll(
                PageRequest.of(page, size, Sort.by(direction, field)));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        course.setCategory(courseDetails.getCategory());
        course.setDifficulty(courseDetails.getDifficulty());
        course.setDuration(courseDetails.getDuration());
        course.setPrice(courseDetails.getPrice());
        course.setPublished(courseDetails.getPublished());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}