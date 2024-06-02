package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;

import java.time.LocalDateTime;
import java.util.List;

public record GetChallengeResponse(
        Long challengeNo,
        String title,
        String content,
        float distance,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String reward,
        List<MemberChallenge> participants
) {
    public static GetChallengeResponse from(Challenge challenge) {
        return new GetChallengeResponse(
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getDistance(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getReward(),
                challenge.getParticipants()
        );
    }
}
