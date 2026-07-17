package com.mangdehenzhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页响应 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long total;
    private int totalPages;

    public static <T> PageDTO<T> of(List<T> content, int page, int size, long total) {
        int totalPages = (size > 0) ? (int) Math.ceil((double) total / size) : 0;
        return new PageDTO<>(content, page, size, total, totalPages);
    }
}