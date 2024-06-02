package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;

import java.time.LocalDateTime;

public record UpdateChallengeResponse(
        Long challengeNo,
        String title,
        String content,
        float distance,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String reward
) {
    public static UpdateChallengeResponse from(Challenge challenge) {
        return new UpdateChallengeResponse(
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getDistance(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getReward()
        );
    }
}
