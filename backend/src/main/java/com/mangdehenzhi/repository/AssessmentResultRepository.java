package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByUserId(Long userId);
    List<AssessmentResult> findByAssessmentId(Long assessmentId);
    List<AssessmentResult> findByUserIdAndPassed(Long userId, Boolean passed);
}