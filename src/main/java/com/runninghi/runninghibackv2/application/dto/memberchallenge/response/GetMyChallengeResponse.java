package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetMyChallengeResponse(
        @Schema(description = "나의 챌린지 Id", example = "1")
        Long memberChallengeId,
        @Schema(description = "연관된 멤버 Id", example = "1")
        Long memberNo,
        @Schema(description = "연관된 챌린지 Id", example = "1")
        Long challengeNo,
        @Schema(description = "챌린지명", example = "1개월 내로 100km 달리기")
        String title,
        @Schema(description = "챌린지 상세정보", example = "목표 거리만큼 달려보세요")
        String content,
        @Schema(description = "챌린지 타입", example = "DISTANCE")
        ChallengeCategory challengeCategory,
        @Schema(description = "챌린지 이미지", example = "test.jpg")
        String imageUrl,
        @Schema(description = "목표 수치", example = "100.0")
        float goal,
        @Schema(description = "목표 상세정보", example = "100.0km")
        String goalDetail,
        @Schema(description = "챌린지 시작일자", example = "2024-06-01T00:00:00")
        LocalDateTime startDate,
        @Schema(description = "챌린지 종료일자", example = "2024-0.6-30T00:00:00")
        LocalDateTime endDate,
        @Schema(description = "챌린지 시작 후 기록", example = "10.528268")
        float record,
        @Schema(description = "챌린지 참여자 수", example = "4132")
        int participantsCount,
        @Schema(description = "전체 회원 랭킹")
        List<GetChallengeRankingResponse> challengeRanking,
        @Schema(description = "로그인한 회원 랭킹")
        GetChallengeRankingResponse memberRanking
) {
    public static GetMyChallengeResponse from(MemberChallenge memberChallenge,
                                              List<GetChallengeRankingResponse> challengeRanking,
                                              GetChallengeRankingResponse memberRanking) {
        Challenge challenge = memberChallenge.getChallenge();

        return new GetMyChallengeResponse(
                memberChallenge.getMemberChallengeId(),
                memberChallenge.getMember().getMemberNo(),
                challenge.getChallengeNo(),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getChallengeCategory(),
                challenge.getImageUrl(),
                challenge.getGoal(),
                challenge.getGoalDetail(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                memberChallenge.getRecord(),
                challenge.getParticipants().size(),
                challengeRanking,
                memberRanking
        );
    }
}

