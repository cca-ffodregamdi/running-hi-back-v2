package com.runninghi.runninghibackv2.application.dto.feedback.response;


import org.springframework.data.domain.Page;

import java.util.List;

public record FeedbackPageResponse<T>(
        List<T> content,
        int currentPage,
        int totalPages
) {
    public static <T> FeedbackPageResponse<T> from(Page<T> page) {
        return new FeedbackPageResponse<>(page.getContent(), page.getNumber() + 1, page.getTotalPages());
    }
}