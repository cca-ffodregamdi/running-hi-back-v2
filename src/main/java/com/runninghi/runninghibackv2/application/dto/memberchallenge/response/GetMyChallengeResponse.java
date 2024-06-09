package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetMyChallengeResponse(
        @Schema(description = "나의 챌린지 Id", example = "1")
        Long memberChallengeId,
        @Schema(description = "연관된 챌린지 Id", example = "1")
        Long challengeNo,
        @Schema(description = "연관된 멤버 Id", example = "1")
        Long memberNo,
        @Schema(description = "챌린지 시작 후 달린 거리", example = "10.528268")
        float distance,
        @Schema(description = "챌린지 시작 후 달린 시간", example = "43.46666")
        float runningTime,
        @Schema(description = "챌린지 시작 후 소모한 칼로리", example = "170.34352")
        float kcal,
        @Schema(description = "챌린지 시작 후 평균 속도", example = "0.214163")
        float speed,
        @Schema(description = "챌린지 시작 후 평균 페이스 (분/km)", example = "4.66935")
        float meanPace,
        @Schema(description = "챌린지 달성 여부", example = "true")
        boolean status
) {
    public static GetMyChallengeResponse from(MemberChallenge memberChallenge) {
        return new GetMyChallengeResponse(
                memberChallenge.getMemberChallengeId(),
                memberChallenge.getChallenge().getChallengeNo(),
                memberChallenge.getMember().getMemberNo(),
                memberChallenge.getDistance(),
                memberChallenge.getRunningTime(),
                memberChallenge.getKcal(),
                memberChallenge.getSpeed(),
                memberChallenge.getMeanPace(),
                memberChallenge.isStatus()
        );
    }
}

