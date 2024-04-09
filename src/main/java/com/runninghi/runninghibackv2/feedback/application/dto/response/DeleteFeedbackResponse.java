package com.runninghi.runninghibackv2.feedback.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드백 삭제 응답")
public record DeleteFeedbackResponse(
        @Schema(description = "삭제된 Feedback Id", example = "1")
        Long feedbackNo
) {
    public static DeleteFeedbackResponse from(Long feedbackNo) {
        return new DeleteFeedbackResponse(feedbackNo);
    }
}
