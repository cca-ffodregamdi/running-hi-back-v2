package com.runninghi.runninghibackv2.application.dto.faq.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FAQ 삭제 응답")
public record DeleteFaqResponse(
        @Schema(description = "FAQ 번호", example = "1")
        Long faqNo
) {
    public static DeleteFaqResponse from(Long faqNo) {
        return new DeleteFaqResponse(faqNo);
    }
}
