package com.mangdehenzhi.controller;

import com.mangdehenzhi.dto.ApiResponse;
import com.mangdehenzhi.dto.PageDTO;
import com.mangdehenzhi.entity.Course;
import com.mangdehenzhi.enums.CourseCategory;
import com.mangdehenzhi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageDTO<Course>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order) {
        var pageResult = courseService.getCoursesPage(page, size, sort, order);
        return ResponseEntity.ok(ApiResponse.success(
                PageDTO.of(pageResult.getContent(), page, size, pageResult.getTotalElements())));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Course>>> getAllPublished() {
        return ResponseEntity.ok(ApiResponse.success(courseService.getAllPublishedCourses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCourseById(id)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByCategory(@PathVariable CourseCategory category) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCoursesByCategory(category)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(ApiResponse.success("课程创建成功", courseService.createCourse(course)));
    }
}