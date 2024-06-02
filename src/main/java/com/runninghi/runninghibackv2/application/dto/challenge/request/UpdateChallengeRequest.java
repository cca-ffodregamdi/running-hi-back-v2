package com.runninghi.runninghibackv2.application.dto.challenge.request;

import java.time.LocalDateTime;

public record UpdateChallengeRequest(
        String title,
        String content,
        float distance,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String reward
) {
}
