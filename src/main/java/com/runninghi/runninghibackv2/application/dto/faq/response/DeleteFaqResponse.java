package com.runninghi.runninghibackv2.application.dto.faq.response;

public record DeleteFaqResponse(
        Long faqNo
) {
    public static DeleteFaqResponse from(Long faqNo) {
        return new DeleteFaqResponse(faqNo);
    }
}
