package com.runninghi.runninghibackv2.application.dto.faq.request;

public record UpdateFaqRequest(
        String question,
        String answer
) {
}
