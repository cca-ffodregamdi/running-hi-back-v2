package com.runninghi.runninghibackv2.feedback.application.dto.response;

public record DeleteFeedbackResponse(
        Long feedbackNo
) {
    public static DeleteFeedbackResponse create(Long feedbackNo) {
        return new DeleteFeedbackResponse(feedbackNo);
    }
}
