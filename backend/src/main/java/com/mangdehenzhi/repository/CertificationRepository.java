package com.mangdehenzhi.repository;

import com.mangdehenzhi.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Optional<Certification> findByCertHash(String certHash);
    List<Certification> findByUserId(Long userId);
    Optional<Certification> findByBlockchainTxId(String blockchainTxId);
}