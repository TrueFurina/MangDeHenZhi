package com.mangdehenzhi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 通用分页请求 DTO
 */
@Data
public class PageRequest {
    @Min(value = 0, message = "页码从0开始")
    private int page = 0;

    @Min(value = 1, message = "每页最少1条")
    @Max(value = 100, message = "每页最多100条")
    private int size = 20;

    private String sortBy = "createdAt";
    private String sortDir = "desc";
}