package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetAllMyChallengeResponse(
        @Schema(description = "나의 챌린지 리스트")
        List<MyChallengeListResponse> myChallengeList,
        @Schema(description = "나의 챌린지 개수", example = "5")
        int myChallengeCount
) {
}
