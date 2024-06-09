package com.runninghi.runninghibackv2.application.dto.notice.response;


import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int currentPage,
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber() + 1, page.getTotalPages());
    }
}