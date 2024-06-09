package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;

import java.time.LocalDateTime;

public record UpdateChallengeResponse(
        Long challengeNo,
        String title,
        String content,
        ChallengeCategory challengeCategory,
        float targetValue,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static UpdateChallengeResponse from(Challenge challenge) {
        return new UpdateChallengeResponse(
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getChallengeCategory(),
                challenge.getTargetValue(),
                challenge.getStartDate(),
                challenge.getEndDate()
        );
    }
}
