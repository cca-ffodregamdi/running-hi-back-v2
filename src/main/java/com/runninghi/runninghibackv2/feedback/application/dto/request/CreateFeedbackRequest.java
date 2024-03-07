package com.runninghi.runninghibackv2.feedback.application.dto.request;

public record CreateFeedbackRequest(
        String title,
        String content,
        int category
) {
}
