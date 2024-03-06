package com.runninghi.runninghibackv2.feedback.application.dto.request;

public record UpdateFeedbackRequest(
        String title,
        String content,
        int category
) {
}
