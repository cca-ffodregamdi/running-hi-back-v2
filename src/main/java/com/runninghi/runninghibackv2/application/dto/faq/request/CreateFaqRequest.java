package com.runninghi.runninghibackv2.application.dto.faq.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FAQ 작성 요청")
public record CreateFaqRequest(
        @Schema(description = "질문", example = "RunningHi가 무엇인가요?")
        String question,
        @Schema(description = "답변", example = "RunningHi는 러닝 앱입니다.")
        String answer
) {
}
