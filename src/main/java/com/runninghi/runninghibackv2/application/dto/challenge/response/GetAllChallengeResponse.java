package com.runninghi.runninghibackv2.application.dto.challenge.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetAllChallengeResponse(
        @Schema(description = "진행중인 챌린지 리스트")
        List<ChallengeListResponse> challengeList,
        @Schema(description = "진행중인 챌린지 개수", example = "5")
        int challengeCount
) {
}
