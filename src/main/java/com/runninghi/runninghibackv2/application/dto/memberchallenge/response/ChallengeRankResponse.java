package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

public record ChallengeRankResponse(
        Long memberChallengeId,
        float record,
        String nickname,
        String profileImageUrl,
        int rank
) {
}
