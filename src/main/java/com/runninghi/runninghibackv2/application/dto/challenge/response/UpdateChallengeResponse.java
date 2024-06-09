package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UpdateChallengeResponse(
        @Schema(description = "챌린지 Id", example = "1")
        Long challengeNo,
        @Schema(description = "챌린지명", example = "1개월 내로 100km 달리기")
        String title,
        @Schema(description = "챌린지 상세정보", example = "목표 거리만큼 달려보세요")
        String content,
        @Schema(description = "챌린지 타입", example = "DISTANCE")
        ChallengeCategory challengeCategory,
        @Schema(description = "목표 수치", example = "100.0")
        float targetValue,
        @Schema(description = "챌린지 시작일자", example = "2024-06-01T00:00:00")
        LocalDateTime startDate,
        @Schema(description = "챌린지 종료일자", example = "2024-0.6-30T00:00:00")
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
