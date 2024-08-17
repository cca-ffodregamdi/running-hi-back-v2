package com.runninghi.runninghibackv2.application.dto.member.response;

public record GetIsTermsAgreedResponse(
        boolean isTermsAgreed
) {
    public static GetIsTermsAgreedResponse of(boolean isTermsAgreed) {
        return new GetIsTermsAgreedResponse(isTermsAgreed);
    }
}
