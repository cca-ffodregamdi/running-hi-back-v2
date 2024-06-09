package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;

import java.time.LocalDateTime;

public record CreateChallengeResponse(
        Long challengeNo,
        String title,
        String content,
        ChallengeCategory challengeCategory,
        float targetValue,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static CreateChallengeResponse from(Challenge challenge) {
        return new CreateChallengeResponse(
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
