package com.runninghi.runninghibackv2.application.dto.faq.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FAQ 수정 요청")
public record UpdateFaqRequest(
        @Schema(description = "질문", example = "RunningHi는 뭘까요?")
        String question,
        @Schema(description = "답변", example = "RunningHi는 러닝 + SNS 앱입니다.")
        String answer
) {
}
