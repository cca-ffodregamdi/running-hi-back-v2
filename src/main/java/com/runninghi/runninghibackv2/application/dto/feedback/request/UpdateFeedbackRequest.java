package com.runninghi.runninghibackv2.application.dto.feedback.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드백 수정 요청")
public record UpdateFeedbackRequest(
        @Schema(description = "제목", example = "서비스 개선 요청 / 문의사항 수정")
        String title,
        @Schema(description = "내용", example = "서비스 이용 중 발견한 문제 / 문의사항에 대한 상세 설명 수정")
        String content,
        @Schema(description = "카테고리", example = "3")
        int category
) {
}
