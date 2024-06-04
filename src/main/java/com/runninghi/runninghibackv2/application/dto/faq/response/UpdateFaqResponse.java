package com.runninghi.runninghibackv2.application.dto.faq.response;

import com.runninghi.runninghibackv2.domain.entity.Faq;

public record UpdateFaqResponse(
        Long faqNo,
        String question,
        String answer
) {
    public static UpdateFaqResponse from(Faq faq) {
        return new UpdateFaqResponse(faq.getFaqNo(), faq.getQuestion(), faq.getAnswer());
    }
}
