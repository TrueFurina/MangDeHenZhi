package com.mangdehenzhi.blockchain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 智能合约数据模型 - 证书存证
 */
@Data
public class CertificationContract {
    private String certHash;
    private Long userId;
    private String userName;
    private String assessmentTitle;
    private Integer score;
    private LocalDateTime issuedAt;
    private String metadata;
}