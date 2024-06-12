package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateMyChallengeResponse(
        @Schema(description = "나의 챌린지 Id", example = "1")
        Long memberChallengeId,
        @Schema(description = "연관된 챌린지 Id", example = "1")
        Long challengeNo,
        @Schema(description = "연관된 멤버 Id", example = "1")
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
