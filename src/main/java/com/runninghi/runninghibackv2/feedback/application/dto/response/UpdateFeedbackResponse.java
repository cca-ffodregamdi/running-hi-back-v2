package com.runninghi.runninghibackv2.feedback.application.dto.response;

public record UpdateFeedbackResponse(
        Long feedbackNo,
        String title,
        String content,
        String category
) {
    public static UpdateFeedbackResponse create(Long feedbackNo, String title, String content, String category) {
        return new UpdateFeedbackResponse(feedbackNo, title, content, category);
    }
}