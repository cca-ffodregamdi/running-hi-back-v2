package com.runninghi.runninghibackv2.feedback.application.dto.response;

import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;

public record CreateFeedbackResponse(
        Long feedbackNo,
        String title,
        String content
) {
    public static CreateFeedbackResponse create(Feedback feedback) {
        return new CreateFeedbackResponse(feedback.getFeedbackNo(), feedback.getTitle(), feedback.getContent());
    }
}
