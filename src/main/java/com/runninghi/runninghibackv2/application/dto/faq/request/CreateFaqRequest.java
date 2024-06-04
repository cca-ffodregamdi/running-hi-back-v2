package com.runninghi.runninghibackv2.application.dto.faq.request;

public record CreateFaqRequest(
        String question,
        String answer
) {
}
