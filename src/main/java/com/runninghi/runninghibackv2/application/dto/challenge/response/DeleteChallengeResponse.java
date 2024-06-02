package com.runninghi.runninghibackv2.application.dto.challenge.response;

public record DeleteChallengeResponse(
        Long challengeNo
) {
    public static DeleteChallengeResponse from(Long challengeNo) {
        return new DeleteChallengeResponse(challengeNo);
    }
}
