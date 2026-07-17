package com.mangdehenzhi.dto;

import lombok.Data;

@Data
public class CreateMetaverseSessionRequest {
    private String sessionName;
    private String sceneType;
    private String sceneConfig;
}