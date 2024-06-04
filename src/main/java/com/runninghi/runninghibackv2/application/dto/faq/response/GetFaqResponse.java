package com.runninghi.runninghibackv2.application.dto.faq.response;

import com.runninghi.runninghibackv2.domain.entity.Faq;

public record GetFaqResponse(
        Long faqNo,
        String question,
        String answer
) {
    public static GetFaqResponse from(Faq faq) {
        return new GetFaqResponse(faq.getFaqNo(), faq.getQuestion(), faq.getAnswer());
    }
}
