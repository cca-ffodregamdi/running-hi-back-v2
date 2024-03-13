package com.runninghi.runninghibackv2.feedback.application.dto.response;

import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;

public record UpdateFeedbackResponse(
        Long feedbackNo,
        String title,
        String content,
        String category
) {
    public static UpdateFeedbackResponse from(Feedback feedback) {
        return new UpdateFeedbackResponse(feedback.getFeedbackNo(), feedback.getTitle(), feedback.getContent(),
                feedback.getCategory().getDescription());
    }
}