package com.runninghi.runninghibackv2.application.dto.faq.response;

import com.runninghi.runninghibackv2.domain.entity.Faq;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FAQ 생성 응답")
public record CreateFaqResponse(
        @Schema(description = "FAQ 번호", example = "1")
        Long faqNo,
        @Schema(description = "질문", example = "RunningHi가 무엇인가요?")
        String question,
        @Schema(description = "답변", example = "RunningHi는 러닝 앱입니다.")
        String answer
) {
    public static CreateFaqResponse from(Faq faq) {
        return new CreateFaqResponse(faq.getFaqNo(), faq.getQuestion(), faq.getAnswer());
    }
}
