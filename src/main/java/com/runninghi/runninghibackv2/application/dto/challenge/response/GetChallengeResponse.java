package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;

import java.time.LocalDateTime;
import java.util.List;

public record GetChallengeResponse(
        Long challengeNo,
        String title,
        String content,
        ChallengeCategory challengeCategory,
        float targetValue,
        LocalDateTime startDate,
        LocalDateTime endDate,
        float totalRunningTime,
        float totalKcal,
        float totalMeanPace,
        List<MemberChallenge> participants
) {
    public static GetChallengeResponse from(Challenge challenge) {
        return new GetChallengeResponse(
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getChallengeCategory(),
                challenge.getTargetValue(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getTotalRunningTime(),
                challenge.getTotalKcal(),
                challenge.getTotalMeanPace(),
                challenge.getParticipants()
        );
    }
}
