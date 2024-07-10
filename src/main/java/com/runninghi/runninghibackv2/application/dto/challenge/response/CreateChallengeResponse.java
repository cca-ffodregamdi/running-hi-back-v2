package com.runninghi.runninghibackv2.application.dto.challenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CreateChallengeResponse(
        @Schema(description = "챌린지 Id", example = "1")
        Long challengeNo,
        @Schema(description = "챌린지명", example = "러닝 초보")
        String title,
        @Schema(description = "챌린지 상세정보", example = "이번 달 10회 이상 달려보세요")
        String content,
        @Schema(description = "챌린지 타입", example = "ATTENDANCE")
        ChallengeCategory challengeCategory,
        @Schema(description = "챌린지 이미지", example = "test.jpg")
        String imageUrl,
        @Schema(description = "목표 수치", example = "10")
        float goal,
        @Schema(description = "목표 상세정보", example = "10회 이상 달리기")
        String goalDetail,
        @Schema(description = "챌린지 시작일자", example = "2024-06-01T00:00:00")
        LocalDateTime startDate,
        @Schema(description = "챌린지 종료일자", example = "2024-0.6-30T00:00:00")
        LocalDateTime endDate
) {
    public static CreateChallengeResponse from(Challenge challenge) {
        return new CreateChallengeResponse(
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getChallengeCategory(),
                challenge.getImageUrl(),
                challenge.getGoal(),
                challenge.getGoalDetail(),
                challenge.getStartDate(),
                challenge.getEndDate()
        );
    }
}
