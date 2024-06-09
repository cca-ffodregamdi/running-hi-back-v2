package com.runninghi.runninghibackv2.application.dto.challenge.request;

import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;

import java.time.LocalDateTime;

public record CreateChallengeRequest(
        String title,
        String content,
        ChallengeCategory challengeCategory,
        float targetValue,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
