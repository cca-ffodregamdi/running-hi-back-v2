package com.runninghi.runninghibackv2.application.dto.member.response;

public record TermsAgreementResponse(
        boolean isTermsAgreed
) {
    public static TermsAgreementResponse of(boolean termsAgreed) {
        return new TermsAgreementResponse(termsAgreed);
    }
}
