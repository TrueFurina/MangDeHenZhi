package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.MetaverseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetaverseSessionRepository extends JpaRepository<MetaverseSession, Long> {
    List<MetaverseSession> findByUserId(Long userId);
    List<MetaverseSession> findByUserIdAndActiveTrue(Long userId);
}