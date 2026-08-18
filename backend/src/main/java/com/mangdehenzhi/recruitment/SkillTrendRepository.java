package com.mangdehenzhi.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillTrendRepository extends JpaRepository<SkillTrend, Long> {
    List<SkillTrend> findByActiveTrueOrderByDemandIndexDesc();
    List<SkillTrend> findByCategoryOrderByDemandIndexDesc(String category);
    List<SkillTrend> findByTrendOrderByDemandIndexDesc(String trend);
    List<SkillTrend> findTop10ByOrderByDemandIndexDesc();
    List<SkillTrend> findTop10ByOrderByScarcityIndexDesc();
    List<SkillTrend> findBySkillNameContainingIgnoreCase(String name);
}