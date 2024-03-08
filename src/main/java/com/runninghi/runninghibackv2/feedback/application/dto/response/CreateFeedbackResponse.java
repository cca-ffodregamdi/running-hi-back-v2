package com.runninghi.runninghibackv2.feedback.application.dto.response;

public record CreateFeedbackResponse(
        Long feedbackNo,
        String title,
        String content
) {
    public static CreateFeedbackResponse create(Long feedbackNo, String title, String content) {
        return new CreateFeedbackResponse(feedbackNo, title, content);
    }
}
