package com.runninghi.runninghibackv2.application.dto.memberchallenge.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateMyChallengeRequest(
        @Schema(description = "연관된 챌린지 Id", example = "1")
        Long challengeNo
) {
}
