package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;

public record GetMyChallengeResponse(
        Long memberChallengeId,
        Long challengeNo,
        Long memberNo,
        float distance,
        float runningTime,
        float kcal,
        float speed,
        float meanPace,
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

