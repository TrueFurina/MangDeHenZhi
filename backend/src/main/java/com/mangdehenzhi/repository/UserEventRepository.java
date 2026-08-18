package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    List<UserEvent> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<UserEvent> findByUserIdAndEventTypeOrderByCreatedAtDesc(Long userId, String eventType);
    long countByUserIdAndEventType(Long userId, String eventType);
    long countByEventTypeAndCreatedAtBetween(String eventType, LocalDateTime start, LocalDateTime end);
    List<UserEvent> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);
}