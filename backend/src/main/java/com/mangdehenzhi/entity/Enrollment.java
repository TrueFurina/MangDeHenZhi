package com.mangdehenzhi.entity;

import com.mangdehenzhi.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程报名记录（选课/实训报名）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    private Long id;
    private Long userId;
    private Long courseId;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}
