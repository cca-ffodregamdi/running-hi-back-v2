package com.runninghi.runninghibackv2.application.dto.feedback.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드백 답변 작성 요청")
public record UpdateFeedbackReplyRequest(
        @Schema(description = "피드백 / 문의사항에 대한 답변", example = "고객님의 문의사항에 대해 답변드립니다.")
        String content
) {
}
