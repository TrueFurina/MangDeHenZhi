package com.mangdehenzhi.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerPathRepository extends JpaRepository<CareerPath, Long> {
    List<CareerPath> findByCategoryOrderByDemandScoreDesc(String category);
    List<CareerPath> findTop10ByOrderByDemandScoreDesc();
    List<CareerPath> findByTitleContainingIgnoreCase(String title);
}