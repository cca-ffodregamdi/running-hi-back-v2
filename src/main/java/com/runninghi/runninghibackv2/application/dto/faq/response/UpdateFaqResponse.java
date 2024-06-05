package com.runninghi.runninghibackv2.application.dto.faq.response;

import com.runninghi.runninghibackv2.domain.entity.Faq;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateFaqResponse(
        @Schema(description = "FAQ 번호", example = "1")
        Long faqNo,
        @Schema(description = "질문", example = "RunningHi는 뭘까요?")
        String question,
        @Schema(description = "답변", example = "RunningHi는 러닝 + SNS 앱입니다.")
        String answer
) {
    public static UpdateFaqResponse from(Faq faq) {
        return new UpdateFaqResponse(faq.getFaqNo(), faq.getQuestion(), faq.getAnswer());
    }
}
