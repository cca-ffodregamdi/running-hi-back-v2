package com.runninghi.runninghibackv2.application.dto.challenge.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteChallengeResponse(
        @Schema(description = "챌린지 Id", example = "1")
        Long challengeNo
) {
    public static DeleteChallengeResponse from(Long challengeNo) {
        return new DeleteChallengeResponse(challengeNo);
    }
}
