package com.runninghi.runninghibackv2.member.application.dto.response;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;

public record GetMemberResponse(
        String nickname,
        int level,
        double totalDistance,
        int distanceToNextLevel,
        Long totalKcal
) {
    public static GetMemberResponse from(Member member) {
        return new GetMemberResponse(member.getNickname(), member.getLevel(),
                member.getTotalDistance(), member.getDistanceToNextLevel(), member.getTotalKcal());
    }
}
