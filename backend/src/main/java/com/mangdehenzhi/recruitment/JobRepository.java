package com.mangdehenzhi.recruitment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByActiveTrue();
    Page<Job> findByActiveTrue(Pageable pageable);
    List<Job> findByCompanyContainingIgnoreCase(String company);
    List<Job> findByTitleContainingIgnoreCase(String title);
    List<Job> findByIndustryContainingIgnoreCase(String industry);
    List<Job> findByLocationContainingIgnoreCase(String location);
    List<Job> findByDegreeContainingIgnoreCase(String degree);
    List<Job> findByMajorContainingIgnoreCase(String major);

    // 复合筛选
    @Query("SELECT j FROM Job j WHERE j.active = true " +
           "AND (:industry IS NULL OR j.industry LIKE %:industry%) " +
           "AND (:location IS NULL OR j.location LIKE %:location%) " +
           "AND (:degree IS NULL OR j.degree LIKE %:degree%) " +
           "AND (:keyword IS NULL OR j.title LIKE %:keyword% OR j.company LIKE %:keyword% OR j.description LIKE %:keyword%)")
    Page<Job> findFiltered(@Param("industry") String industry,
                           @Param("location") String location,
                           @Param("degree") String degree,
                           @Param("keyword") String keyword,
                           Pageable pageable);
}