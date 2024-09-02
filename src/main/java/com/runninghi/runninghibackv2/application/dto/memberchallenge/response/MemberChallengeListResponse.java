package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.time.LocalDateTime;

public record MemberChallengeListResponse(
        @Schema(description = "연관된 챌린지 Id", example = "1")
        Long challengeId,
        @Schema(description = "나의 챌린지 Id", example = "1")
        Long memberChallengeId,
        @Schema(description = "챌린지명", example = "1개월 내로 100km 달리기")
        String title,
        @Schema(description = "챌린지 이미지", example = "test.jpg")
        String imageUrl,
        @Schema(description = "챌린지 시작일자", example = "2024-06-01T00:00:00")
        LocalDateTime startDate,
        @Schema(description = "챌린지 종료일자", example = "2024-06-30T00:00:00")
        LocalDateTime endDate,
        @Schema(description = "챌린지 남은기간", example = "20")
        Long remainingTime,
        @Schema(description = "챌린지 참여자 수", example = "4132")
        int participantsCount
) {
    public static MemberChallengeListResponse from(MemberChallenge memberChallenge) {
        Challenge challenge = memberChallenge.getChallenge();
        Duration duration = Duration.between(LocalDateTime.now(), challenge.getEndDate());
        Long remainingTime = duration.toDays() >= 0 ? duration.toDays() : 0;

        return new MemberChallengeListResponse(
                challenge.getChallengeNo(),
                memberChallenge.getMemberChallengeId(),
                challenge.getTitle(),
                challenge.getImageUrl(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                remainingTime,
                challenge.getParticipants().size()
        );
    }
}
