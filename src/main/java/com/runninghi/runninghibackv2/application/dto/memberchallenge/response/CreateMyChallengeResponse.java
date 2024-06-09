package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;

public record CreateMyChallengeResponse(
        Long memberChallengeId,
        Long challengeNo,
        Long memberNo
) {
    public static CreateMyChallengeResponse from(MemberChallenge memberChallenge) {
        return new CreateMyChallengeResponse(
                memberChallenge.getMemberChallengeId(),
                memberChallenge.getChallenge().getChallengeNo(),
                memberChallenge.getMember().getMemberNo()
        );
    }
}
