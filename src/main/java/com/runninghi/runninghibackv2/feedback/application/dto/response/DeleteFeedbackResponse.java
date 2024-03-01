package com.runninghi.runninghibackv2.feedback.application.dto.response;

import com.runninghi.runninghibackv2.feedback.application.dto.request.DeleteFeedbackRequest;

public record DeleteFeedbackResponse(
        Long feedbackNo
) {
    public static DeleteFeedbackResponse create(Long feedbackNo) {
        return new DeleteFeedbackResponse(feedbackNo);
    }
}
