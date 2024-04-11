package com.runninghi.runninghibackv2.application.dto.member.response;

import com.runninghi.runninghibackv2.domain.entity.Member;

public record GetMemberResponse(
        String nickname,
        int level,
        double totalDistance,
        int distanceToNextLevel,
        double totalKcal
) {
    public static GetMemberResponse from(Member member) {
        return new GetMemberResponse(member.getNickname(), member.getLevel(),
                member.getTotalDistance(), member.getDistanceToNextLevel(), member.getTotalKcal());
    }
}
