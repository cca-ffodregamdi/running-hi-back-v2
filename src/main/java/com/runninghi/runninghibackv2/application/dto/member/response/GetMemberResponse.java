package com.runninghi.runninghibackv2.application.dto.member.response;

import com.runninghi.runninghibackv2.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 조회(마이 페이지) 응답")
public record GetMemberResponse(
        @Schema(description = "회원 번호", example = "1")
        Long memberNo,
        @Schema(description = "닉네임", example = "러너 23139403")
        String nickname,
        @Schema(description = "레벨", example = "5")
        int level,
        @Schema(description = "회원이 달린 총 거리", example = "200.3")
        double totalDistance,
        @Schema(description = "다음 레벨 거리", example = "1000")
        int distanceToNextLevel,
        @Schema(description = "총 소모 칼로리", example = "500.1")
        double totalKcal
) {
    public static GetMemberResponse from(Member member) {
        return new GetMemberResponse(member.getMemberNo(), member.getNickname(), member.getRunDataVO().getLevel(),
                member.getRunDataVO().getTotalDistance(), member.getRunDataVO().getDistanceToNextLevel(),
                member.getRunDataVO().getTotalKcal());
    }
}
