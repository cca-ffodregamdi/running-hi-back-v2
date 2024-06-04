package com.runninghi.runninghibackv2.application.dto.faq.response;

import com.runninghi.runninghibackv2.domain.entity.Faq;

public record CreateFaqResponse(
        Long faqNo,
        String question,
        String answer
) {
    public static CreateFaqResponse from(Faq faq) {
        return new CreateFaqResponse(faq.getFaqNo(), faq.getQuestion(), faq.getAnswer());
    }
}
