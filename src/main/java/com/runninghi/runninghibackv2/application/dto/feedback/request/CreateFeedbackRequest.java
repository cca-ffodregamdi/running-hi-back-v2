package com.runninghi.runninghibackv2.application.dto.feedback.request;

import com.runninghi.runninghibackv2.domain.enumtype.FeedbackCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드백 작성 요청")
public record CreateFeedbackRequest(
        @Schema(description = "피드백 제목", example = "서비스 개선 요청 / 문의사항")
        String title,
        @Schema(description = "피드백 제목", example = "서비스 이용 중 발견한 문제 / 문의사항에 대한 상세 설명")
        String content,
        @Schema(description = "피드백 카테고리", example = "INQUIRY")
        FeedbackCategory category
) {
}
